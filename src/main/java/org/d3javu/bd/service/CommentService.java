package org.d3javu.bd.service;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.comment.CommentCreateDto;
import org.d3javu.bd.dto.comment.CommentDtoForLargeQuery;
import org.d3javu.bd.dto.comment.CommentEditDto;
import org.d3javu.bd.dto.comment.CommentReadDto;
import org.d3javu.bd.dto.user.CompactUserReadDto;
import org.d3javu.bd.mapper.comment.CommentCreateMapper;
import org.d3javu.bd.mapper.comment.CommentEditMapper;
import org.d3javu.bd.mapper.comment.CommentReadMapper;
import org.d3javu.bd.models.comment.Comment;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.repositories.CommentRepository;
import org.d3javu.bd.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentCreateMapper commentCreateMapper;
    private final CommentEditMapper commentEditMapper;
    private final CommentReadMapper commentReadMapper;
    private final UserService userService;

    private final PostService postService;


//crud

    @Transactional
    public CommentReadDto create(Long postId, CommentCreateDto commentCreateDto) {
//        var comment = commentCreateMapper.map(commentCreateDto);
//        comment = commentRepository.saveAndFlush(comment);
//        var comment = this.addComment(postId, commentCreateDto);
//        var comment = this.commentRepository.saveAndFlush(this.commentCreateMapper.map(commentCreateDto));
//        return this.commentRepository.saveAllAndFlush(comment)
//        var comment = this.commentCreateMapper.map(commentCreateDto);
        commentCreateDto.setPostId(postId);
        commentCreateDto.setCreatedAt(LocalDateTime.now());
        commentCreateDto.setLikesCount(0L);
        var id = this.commentRepository.createComment(
                commentCreateDto.body,
                commentCreateDto.postId,
                commentCreateDto.userId,
                commentCreateDto.createdAt,
                commentCreateDto.likesCount
        );



        return new CommentReadDto(Long.valueOf(id), null, null, null, null, null);

//        return commentReadMapper.map(comment);
    }
//
//    public Optional<CommentReadDto> findById(Long id) {
//        return commentRepository.findById(id).map(commentReadMapper::map);
//    }

//    public Set<CommentReadDto> findAll() {
//        return this.commentRepository.findAll()
//                .stream().map(commentReadMapper::map).collect(Collectors.toSet());
//    }

    public Set<CommentDtoForLargeQuery> findAllByPostId(Long postId, Long userId) {
        Set<CommentDtoForLargeQuery> comments = new HashSet<>();
        var vals = this.commentRepository.getIdAndBodyAndCreatedAtAndAuthorIdByPostId(postId);
        for (var x : vals) {
            var commentId = (Long)x[0];
            var commentBody = (String)x[1];
            var commentCreatedAt = ((Timestamp) x[2]).toLocalDateTime();
            var commentAuthorId = (Long)x[3];
            var authorRaw = this.userRepository.FindFirstNameAndLastNameAndAvatarPathById(commentAuthorId)[0].split(",");
            var author = new CompactUserReadDto(commentAuthorId, (String)authorRaw[0], (String)authorRaw[1], (String)authorRaw[2] );
            var likes = this.commentRepository.getLikesCountById(commentId);
            var isLiked = this.commentRepository.existsLikeByUserIdAndCommentId(userId, commentId);

            var unit = new CommentDtoForLargeQuery(commentId, commentBody, commentCreatedAt, postId, author, likes, isLiked);
            comments.add(unit);
        }

        return comments;
    }

    public CommentDtoForLargeQuery findById(Long commentId, Long userId) {
        return this.commentRepository.getIdAndBodyAndCreatedAtAndAuthorIdByCommentId(commentId)
                .stream()
                .map(en -> {
                    var comment = new CommentDtoForLargeQuery();
                    comment.id = (Long)en[0];
                    comment.body = (String)en[1];
                    comment.createdAt = ((Timestamp) en[2]).toLocalDateTime();
                    comment.likesCount = (Long)en[3];
                    comment.postId = (Long)en[4];
                    comment.author = new CompactUserReadDto((Long)en[5], (String)en[6], (String)en[7], (String)en[8]);
                    comment.isLiked = this.commentRepository.existsLikeByUserIdAndCommentId(userId, commentId);
                    return comment;
                })
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Comment not found"));
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
    public void like(Long commentId, Long userId) {
        this.commentRepository.likeComment(userId, commentId);
        this.commentRepository.updateLikesCountByCommentId(commentId);
    }

    @Transactional
    public void unlike(Long commentId, Long userId) {
        this.commentRepository.unlikeComment(userId, commentId);
        this.commentRepository.updateLikesCountByCommentId(commentId);
    }

//    @Transactional
//    public Comment addComment(Long postId, CommentCreateDto commentCreateDto) {
//        var post = this.postService.findPostById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
////        comment.setPost(post);
//        commentCreateDto.post = post;
//        var comment = this.commentCreateMapper.map(commentCreateDto);
//        return this.commentRepository.save(comment);
////        this.postService.internalUpdate(postId, post);
//    }


}
