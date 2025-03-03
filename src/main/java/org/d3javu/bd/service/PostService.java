package org.d3javu.bd.service;

import org.d3javu.bd.dto.post.PostCreateDto;
import org.d3javu.bd.dto.post.PostEditDto;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.filter.EPredicateBuildMethod;
import org.d3javu.bd.filter.post.PostFilter;
import org.d3javu.bd.mapper.post.PostCreateMapper;
import org.d3javu.bd.mapper.post.PostEditMapper;
import org.d3javu.bd.mapper.post.PostForReportMapper;
import org.d3javu.bd.mapper.post.PostReadMapper;
import org.d3javu.bd.models.post.PostForReport;
import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.repositories.PostRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
//@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostEditMapper postEditMapper;
    private final PostReadMapper postReadMapper;
    private final ImageService imageService;
    private final PostForReportMapper postForReportMapper;
    private final UserService userService;
    private final TagService tagService;

    public PostService(PostRepository postRepository,
                       PostEditMapper postEditMapper,
                       PostReadMapper postReadMapper,
                       ImageService imageService,
                       PostForReportMapper postForReportMapper,
                       @Lazy UserService userService,
                       TagService tagService) {
        this.postRepository = postRepository;
        this.postEditMapper = postEditMapper;
        this.postReadMapper = postReadMapper;
        this.imageService = imageService;
        this.postForReportMapper = postForReportMapper;
        this.userService = userService;
        this.tagService = tagService;
    }

    //crud
    @Transactional
    public Long create(PostCreateDto postCreateDto, Long userId) {
        var images = Optional.ofNullable(postCreateDto.getImages()).orElse(new ArrayList<>());
        var postId = this.postRepository.create(
                postCreateDto.title,
                postCreateDto.body,
                userId,
                LocalDateTime.now(),
                0L, 0L
        );
        this.uploadImages(images, postId);
        return postId;
    }

    public Optional<PostReadDto> findById(Long id) {
        return this.postRepository.findById(id).map(this.postReadMapper::map);
    }
    public Optional<PostReadDto> findPostById(Long postId, Long userId) {
        var p = this.postRepository.findAllReadDtoWithAuthorById(postId).get(0);
        Long l = (Long)p[0];
//        System.out.println(l);
        return this.postRepository.findAllReadDtoWithAuthorById(postId)
                .stream()
                .map(en ->{
                    var t = this.dtoFromObjectArr(en);
                    t.setTags(this.tagService.findByPost(t.getId()));
                    t.setIsLiked(this.postRepository.existsLikeByPostIdAndUserId(t.getId(), userId));
                    t.setImages(this.imageService.findAllImagesByPostId(t.getId()));
                    return t;
                }).findFirst();
    }


//    public List<PostReadDto> findAll() {
//        return this.postRepository.findAll().stream().map(this.postReadMapper::map).toList();
//    }

    public List<PostReadDto> findAll(Long userId) {
        return this.postRepository.findAllReadDtoWithAuthor()
                .stream()
                .map(en ->{
                    var t = this.dtoFromObjectArr(en);
                    t.setTags(this.tagService.findByPost(t.getId()));
                    t.setIsLiked(this.postRepository.existsLikeByPostIdAndUserId(t.getId(), userId));
                    t.setImages(this.imageService.findAllImagesByPostId(t.getId()));
                    return t;
                })
                .collect(Collectors.toList());
    }

    public List<PostReadDto> findByPreferred(Set<Tag> preferredTags, Long userId) {
        var postFilter = new PostFilter(preferredTags);
        return this.postRepository.findDtosByIdIn((ArrayList<Long>)this.postRepository.findAllIdsByTagsFilter(postFilter, EPredicateBuildMethod.OR))
                .stream()
                .map(en -> {
//                    var t = new PostReadDto(
//                            en.getId(),
//                            en.getTitle(),
//                            en.getBody(),
//                            en.getCreatedAt(),
//                            en.getLikesCount(),
//                            en.getAuthor().getId(),
//                            en.getAuthor().getFirstName(),
//                            en.getAuthor().getLastName(),
//                            en.getAuthor().getAvatarPath()
//
//                    );
                    var t = this.dtoFromObjectArr(en);
                    t.setTags(this.tagService.findByPost(t.getId()));
                    t.setIsLiked(this.postRepository.existsLikeByPostIdAndUserId(t.getId(), userId));
                    t.setImages(this.imageService.findAllImagesByPostId(t.getId()));
                    return t;
                }).toList();
    }

    public List<PostReadDto> findByTags(Set<Tag> tags) {
        var postFilter = new PostFilter(tags);
        return this.postRepository.findDtosByIdIn((ArrayList<Long>) this.postRepository.findAllIdsByTagsFilter(postFilter, EPredicateBuildMethod.AND))
                .stream()
                .map(en -> {
//                    var t = new PostReadDto(
//                        en.getId(),
//                        en.getTitle(),
//                        en.getBody(),
//                        en.getCreatedAt(),
//                        en.getLikesCount(),
//                        en.getAuthor().getId(),
//                        en.getAuthor().getFirstName(),
//                        en.getAuthor().getLastName(),
//                        en.getAuthor().getAvatarPath()
//                    );
                    var t = this.dtoFromObjectArr(en);
                    t.setTags(this.tagService.findByPost(t.getId()));
                    t.setImages(this.imageService.findAllImagesByPostId(t.getId()));
                    t.setIsLiked(this.postRepository.existsLikeByPostIdAndUserId(t.id, this.getCurrentUserId()));
                    return t;
                }).toList();
    }

    public List<PostReadDto> findAllByTagId(Long tagId) {
        return this.postRepository.findAllByTagId(tagId)
                .stream().map(postReadMapper::map)
                .toList();
    }

    public List<PostForReport> findAllByIds(Set<Long> ids) {
        return this.postRepository.findAllByIdIn(ids)
                .stream()
                .map(this.postForReportMapper::map)
                .toList();
    }

    @Transactional
    public Optional<PostReadDto> update(Long id, PostEditDto postEditDto) {
        return this.postRepository.findById(id)
                .map(en -> {
                    this.uploadImages(postEditDto.getImages(), en.getId());
                    this.postRepository.saveAndFlush(this.postEditMapper.map(postEditDto, en));
                    return this.postReadMapper.map(this.postRepository.findById(id).get());
        });
//        return Optional.ofNullable(this.postReadMapper.map(this.postRepository.findById(id).get()));
//        post.get().getImages().clear();
//        return Optional.of(this.postReadMapper.map(post.get()));

//        return this.postRepository.findById(id)
//                .map(post -> this.postEditMapper.map(postEditDto, post))
//                .map(e -> {
//                    this.uploadImages(postEditDto.images);
//                })
//                .map(this.postRepository::saveAndFlush)
//                .map(this.postReadMapper::map);
    }

//    @Transactional
//    public Optional<PostReadDto> internalUpdate(Long id, Post post) {
//        return Optional.of(this.postReadMapper.map(this.postRepository.saveAndFlush(post)));
////                .map(post -> this.postEditMapper.map(postEditDto, post))
////                .map(this.postRepository::saveAndFlush)
////                .map(this.postReadMapper::map);
//    }

    @Transactional
    public boolean delete(Long id) {
        return this.postRepository.findById(id)
                .map(post -> {
                    this.postRepository.delete(post);
                    this.postRepository.flush();
                    return true;
                })
                .orElse(false);
    }
//crud end

    @Transactional
    public void view(Long id, User user) {
        var post = postRepository.findById(id).orElse(null);
        if(post != null) {
            post.view(user);
            postRepository.saveAndFlush(post);
        }else{
            throw new NotFoundException("Post not found");
        }
    }

    @Transactional
    public void like(Long postId, Long userId) {
        if(this.postRepository.existsPostById(postId) && !this.postRepository.existsLikeByPostIdAndUserId(postId, userId)){
                this.postRepository.likePost(postId, userId);
                this.postRepository.updateLikesCount(postId);
        } else{
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }

    }

    @Transactional
    public void unlike(Long postId, Long userId){
        if(this.postRepository.existsPostById(postId) && this.postRepository.existsLikeByPostIdAndUserId(postId, userId)){
            this.postRepository.unlikePost(postId, userId);
            this.postRepository.updateLikesCount(postId);
        } else{
          throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
    }

        public List<PostForReport> findTopN(int n){
        return this.postRepository.findAllByOrderByLikesCountDesc(PageRequest.of(0, n))
                .stream()
                .map(this.postForReportMapper::map)
                .collect(Collectors.toList());
    }

    public void uploadImages(List<MultipartFile> images, Long postId){
        for(var x : images){
            if(!x.isEmpty()){
                try {
                    imageService.uploadImage(x.getOriginalFilename(), x.getInputStream(), postId);
                }
                catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);//something other
                }
            }
        }
    }

    public List<byte[]> findImages(Long id){

        var post = this.postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var images = post.getImages();
//        System.out.println(images);
        List<byte[]> imagesList = new ArrayList<>(images.size());
        for (int i = 0; i < images.size(); i++) {
            imagesList.add(this.imageService.getImage(images.get(i).getPath()).orElse(null));
        }
        return imagesList;
//        return this.postRepository.findById(id)
//                .map(Post::getImages)
//                .filter(StringUtils::hasText)
//                .flatMap(imageService::getImage);
    }


    public Set<PostReadDto> findAllByAuthorId(long id) {
        return this.postRepository.findAllByAuthorId(id)
                .stream()
                .map(en -> {
                    var t = this.dtoFromObjectArr(en);
                    t.setTags(this.tagService.findByPost(t.getId()));
                    t.setImages(this.imageService.findAllImagesByPostId(t.getId()));
                    t.setIsLiked(this.postRepository.existsLikeByPostIdAndUserId(t.id, this.getCurrentUserId()));
                    return t;
                })
                .collect(Collectors.toSet());
    }

    public Long getCurrentUserId(){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findIdByEmail(userEmail);
    }

    private PostReadDto dtoFromObjectArr(Object[] en){
        return new PostReadDto(
                (Long) en[0],
                (String) en[1],
                (String) en[2],
                LocalDateTime.ofInstant(((Timestamp)en[3]).toInstant(), ZoneOffset.UTC),
                (Long) en[4],
                (Long) en[5],
                (String) en[6],
                (String) en[7],
                (String) en[8]

        );
    }

}
