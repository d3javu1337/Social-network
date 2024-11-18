package org.d3javu.bd.service;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.comment.CommentReadDto;
import org.d3javu.bd.dto.post.PostCreateDto;
import org.d3javu.bd.dto.post.PostEditDto;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.filter.post.PostFilter;
import org.d3javu.bd.mapper.post.PostCreateMapper;
import org.d3javu.bd.mapper.post.PostEditMapper;
import org.d3javu.bd.mapper.post.PostReadMapper;
import org.d3javu.bd.mapper.post.StaticPostCreateMapper;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.repositories.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostCreateMapper postCreateMapper;
    private final PostEditMapper postEditMapper;
    private final PostReadMapper postReadMapper;


//crud
    @Transactional
    public PostReadDto create(PostCreateDto postCreateDto) {
        var post = Optional.of(postCreateDto).map(this.postCreateMapper::map).get();
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
        return this.postRepository.findAllByOrderByCreatedAtAsc().stream().map(this.postReadMapper::map).toList();
    }

    public List<PostReadDto> findByPreferred(Set<Tag> preferredTags) {
        var postFilter = new PostFilter(preferredTags);
        return this.postRepository.findAllByTagsFilter(postFilter)
                .stream().map(this.postReadMapper::map).toList();
    }

    public List<PostReadDto> findAllByTagId(Long tagId) {
        return this.postRepository.findAllByTagId(tagId)
                .stream().map(postReadMapper::map)
                .toList();
    }

    @Transactional
    public Optional<PostReadDto> update(Long id, PostEditDto postEditDto) {
        return this.postRepository.findById(id)
                .map(post -> this.postEditMapper.map(postEditDto, post))
                .map(this.postRepository::saveAndFlush)
                .map(this.postReadMapper::map);
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



}
