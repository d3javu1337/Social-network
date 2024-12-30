package org.d3javu.bd.service;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.post.PostCreateDto;
import org.d3javu.bd.dto.post.PostEditDto;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.filter.EPredicateBuildMethod;
import org.d3javu.bd.filter.post.PostFilter;
import org.d3javu.bd.mapper.post.PostCreateMapper;
import org.d3javu.bd.mapper.post.PostEditMapper;
import org.d3javu.bd.mapper.post.PostForReportMapper;
import org.d3javu.bd.mapper.post.PostReadMapper;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.post.PostForReport;
import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.repositories.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostCreateMapper postCreateMapper;
    private final PostEditMapper postEditMapper;
    private final PostReadMapper postReadMapper;
    private final ImageService imageService;
    private final PostForReportMapper postForReportMapper;


    //crud
    @Transactional
    public PostReadDto create(PostCreateDto postCreateDto) {
        var images = Optional.ofNullable(postCreateDto.getImages()).orElse(new ArrayList<>());
        var post = Optional.of(postCreateDto).map(this.postCreateMapper::map).get();
        this.uploadImages(images);
        post = this.postRepository.saveAndFlush(post);
        return this.postReadMapper.map(post);
    }

    public Optional<PostReadDto> findById(Long id) {
        return this.postRepository.findById(id).map(this.postReadMapper::map);
    }
    public Optional<Post> findPostById(Long id) {
        return this.postRepository.findById(id);
    }


    public List<PostReadDto> findAll() {
        return this.postRepository.findAll().stream().map(this.postReadMapper::map).toList();
    }

    public List<PostReadDto> findByPreferred(Set<Tag> preferredTags) {
        var postFilter = new PostFilter(preferredTags);
        return this.postRepository.findAllByTagsFilter(postFilter, EPredicateBuildMethod.OR)
                .stream().map(this.postReadMapper::map).toList();
    }

    public List<PostReadDto> findByTags(Set<Tag> tags) {
        var postFilter = new PostFilter(tags);
        return this.postRepository.findAllByTagsFilter(postFilter, EPredicateBuildMethod.AND)
                .stream().map(this.postReadMapper::map).toList();
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
        var post = this.findPostById(id);
        post.get().getImages().clear();
        this.uploadImages(postEditDto.getImages());
        this.postRepository.saveAndFlush(this.postEditMapper.map(postEditDto, post.get()));
        return Optional.ofNullable(this.postReadMapper.map(this.postRepository.findById(id).get()));
//        return Optional.of(this.postReadMapper.map(post.get()));

//        return this.postRepository.findById(id)
//                .map(post -> this.postEditMapper.map(postEditDto, post))
//                .map(e -> {
//                    this.uploadImages(postEditDto.images);
//                })
//                .map(this.postRepository::saveAndFlush)
//                .map(this.postReadMapper::map);
    }

    @Transactional
    public Optional<PostReadDto> internalUpdate(Long id, Post post) {
        return Optional.of(this.postReadMapper.map(this.postRepository.saveAndFlush(post)));
//                .map(post -> this.postEditMapper.map(postEditDto, post))
//                .map(this.postRepository::saveAndFlush)
//                .map(this.postReadMapper::map);
    }

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
    public void like(Long id, User user){
        var post = postRepository.findById(id).orElse(null);
        if(post != null) {
            post.like(user);
            postRepository.saveAndFlush(post);
        }else{
            throw new NotFoundException("Post not found");
        }
    }

    @Transactional
    public void unlike(Long id, User user){
        var post = postRepository.findById(id).orElse(null);
        if(post != null) {
            post.unlike(user);
            postRepository.saveAndFlush(post);
        }else{
            throw new NotFoundException("Post not found");
        }
    }

        public List<PostForReport> findTopN(int n){
        return this.postRepository.findAllByOrderByLikesCountDesc(PageRequest.of(0, n))
                .stream()
                .map(this.postForReportMapper::map)
                .collect(Collectors.toList());
    }

    public void uploadImages(List<MultipartFile> images){
        for(var x : images){
            if(!x.isEmpty()){
                try {
                    imageService.uploadImage(x.getOriginalFilename(), x.getInputStream());
//                    post.se
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
                .map(this.postReadMapper::map)
                .collect(Collectors.toSet());
    }
}
