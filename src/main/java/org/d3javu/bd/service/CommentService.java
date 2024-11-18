package org.d3javu.bd.service;

import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.comment.CommentCreateDto;
import org.d3javu.bd.dto.comment.CommentEditDto;
import org.d3javu.bd.dto.comment.CommentReadDto;
import org.d3javu.bd.mapper.comment.CommentCreateMapper;
import org.d3javu.bd.mapper.comment.CommentEditMapper;
import org.d3javu.bd.mapper.comment.CommentReadMapper;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.repositories.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentCreateMapper commentCreateMapper;
    private final CommentEditMapper commentEditMapper;
    private final CommentReadMapper commentReadMapper;

    private final PostService postService;


//crud

    @Transactional
    public CommentReadDto create(Long postId, CommentCreateDto commentCreateDto) {
        var comment = commentCreateMapper.map(commentCreateDto);
//        comment = commentRepository.saveAndFlush(comment);
        this.addComment(postId, commentCreateDto);
        return commentReadMapper.map(comment);
    }

    public Optional<CommentReadDto> findById(Long id) {
        return commentRepository.findById(id).map(commentReadMapper::map);
    }

    public Set<CommentReadDto> findAll() {
        return this.commentRepository.findAll()
                .stream().map(commentReadMapper::map).collect(Collectors.toSet());
    }

    public Set<CommentReadDto> findAllByPostId(Long id) {
        return this.commentRepository.findAllByPostId(id)
                .stream().map(commentReadMapper::map)
                .collect(Collectors.toSet());
    }

    @Transactional
    public Optional<CommentReadDto> update(Long id, CommentEditDto commentEditDto) {
        return this.commentRepository.findById(id)
                .map(comment -> this.commentEditMapper.map(commentEditDto, comment))
                .map(this.commentRepository::saveAndFlush)
                .map(commentReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return this.commentRepository.findById(id)
                .map(comment -> {
                    this.commentRepository.delete(comment);
                    this.commentRepository.flush();
                    return true;
                })
                .orElse(false);
    }

//crud

    @Transactional
    public void like(Long id, User user) {
        this.commentRepository.findById(id).map(comment -> {
            comment.like(user);
            this.commentRepository.save(comment);
            this.commentRepository.flush();
            return true;
        }).orElseThrow(() -> new NotFoundException("Comment not found"));
    }

    @Transactional
    public void unlike(Long id, User user) {
        this.commentRepository.findById(id).map(comment -> {
            comment.unlike(user);
            this.commentRepository.save(comment);
            this.commentRepository.flush();
            return true;
        }).orElseThrow(() -> new NotFoundException("Comment not found"));
    }

    @Transactional
    public void addComment(Long postId, CommentCreateDto commentCreateDto) {
        var post = this.postService.findPostById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
//        comment.setPost(post);
        commentCreateDto.post = post;
        var comment = this.commentCreateMapper.map(commentCreateDto);
        this.commentRepository.save(comment);
//        this.postService.internalUpdate(postId, post);
    }


}
