package org.d3javu.bd.service;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.comment.CommentCreateDto;
import org.d3javu.bd.dto.comment.CommentReadDto;
import org.d3javu.bd.dto.comment.CommentEditDto;
import org.d3javu.bd.mapper.comment.CommentReadMapper;
import org.d3javu.bd.repositories.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentReadMapper commentReadMapper;


    @Transactional
    public CommentReadDto create(Long postId, Long userId, CommentCreateDto commentCreateDto) {
        commentCreateDto.setPostId(postId);
        commentCreateDto.setCreatedAt(LocalDateTime.now());
        commentCreateDto.setLikesCount(0L);
        commentCreateDto.setUserId(userId);
        var id = this.commentRepository.createComment(
                commentCreateDto.body,
                commentCreateDto.postId,
                commentCreateDto.userId,
                commentCreateDto.createdAt,
                commentCreateDto.likesCount
        );
        return this.findById(id, userId);
    }

    public Set<CommentReadDto> findAllByPostId(Long postId, Long userId) {
        Set<CommentReadDto> comments = new HashSet<>();
//        var vals = this.commentRepository.getIdAndBodyAndCreatedAtAndAuthorIdByPostId(postId);
//        for (var x : vals) {
//            var commentId = (Long)x[0];
//            var commentBody = (String)x[1];
//            var commentCreatedAt = ((Timestamp) x[2]).toLocalDateTime();
//            var commentAuthorId = (Long)x[3];
//            var authorRaw = this.userRepository.FindFirstNameAndLastNameAndAvatarPathById(commentAuthorId)[0].split(",");
//            var author = new CompactUserReadDto(commentAuthorId, (String)authorRaw[0], (String)authorRaw[1], (String)authorRaw[2] );
//            var likes = this.commentRepository.getLikesCountById(commentId);
//            var isLiked = this.commentRepository.existsLikeByUserIdAndCommentId(userId, commentId);
//
//            var unit = new CommentReadDtoV2(commentId, commentBody, commentCreatedAt, postId, author, likes, isLiked);
//            comments.add(unit);
//        }

        return this.commentRepository.findAllDtoByPostId(postId)
                .stream()
                .map(en -> {
                    var comm = this.commentReadMapper.map(en);
                    comm.isLiked = this.commentRepository.existsLikeByUserIdAndCommentId(userId, comm.id);
                    return comm;
                })
                .collect(Collectors.toSet());


    }

    public CommentReadDto findById(Long commentId, Long userId) {
        return this.commentRepository.findCommentById(commentId)
                .map(en -> {
                    var comment = this.commentReadMapper.map(en);
                    comment.isLiked = this.commentRepository.existsLikeByUserIdAndCommentId(userId, commentId);
                    return comment;
                }).orElseThrow(() -> new NotFoundException("Comment not found"));
    }

    @Transactional
    public CommentReadDto update(Long commentId, Long currentUserId, CommentEditDto commentEditDto) {
        this.commentRepository.updateComment(commentId, commentEditDto.body);
        return this.findById(commentId, currentUserId);
    }

    @Transactional
    public void delete(Long id) {
        this.commentRepository.deleteCommentById(id);
    }

    @Transactional
    public void like(Long commentId, Long userId) {
        if(this.commentRepository.existsLikeByUserIdAndCommentId(userId, commentId)) {
            return;
        }
        this.commentRepository.likeComment(userId, commentId);
        this.commentRepository.updateLikesCountByCommentId(commentId);
    }

    @Transactional
    public void unlike(Long commentId, Long userId) {
        if(!this.commentRepository.existsLikeByUserIdAndCommentId(userId, commentId)) {
            return;
        }
        this.commentRepository.unlikeComment(userId, commentId);
        this.commentRepository.updateLikesCountByCommentId(commentId);
    }
}
