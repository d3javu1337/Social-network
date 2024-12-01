package org.d3javu.bd.mapper.comment;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.comment.CommentReadDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.mapper.post.PostReadMapper;
import org.d3javu.bd.mapper.user.UserMapper;
import org.d3javu.bd.mapper.user.UserReadMapper;
import org.d3javu.bd.models.comment.Comment;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.user.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

//@RequiredArgsConstructor
@Component
public class CommentReadMapper implements Mapper<Comment, CommentReadDto> {

    private final UserReadMapper userReadMapper;
    private final PostReadMapper postReadMapper;

    CommentReadMapper(@Lazy final UserReadMapper userReadMapper, @Lazy final PostReadMapper postReadMapper) {
        this.userReadMapper = userReadMapper;
        this.postReadMapper = postReadMapper;
    }

    @Override
    public CommentReadDto map(Comment object) {
        return new CommentReadDto(
                object.getId(),
                object.getBody(),
                this.postReadMapper.map(object.getPost()),
                this.userReadMapper.map(object.getUser()),
                object.getLikes().stream().map(this.userReadMapper::map).collect(Collectors.toSet()),
//                Optional.ofNullable(object.getLikes()).map(this.userMapper::map).orElse(new HashSet<>()),
                object.getCreatedAt()
        );
    }

}