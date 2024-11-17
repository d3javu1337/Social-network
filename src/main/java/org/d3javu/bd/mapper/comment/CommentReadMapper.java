package org.d3javu.bd.mapper.comment;

import org.d3javu.bd.dto.comment.CommentReadDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.comment.Comment;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.user.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Component
public class CommentReadMapper implements Mapper<Comment, CommentReadDto> {

    @Override
    public CommentReadDto map(Comment object) {
        return new CommentReadDto(
                object.getId(),
                object.getBody(),
                object.getPost(),
                object.getUser(),
                Optional.ofNullable(object.getLikes()).orElse(new HashSet<>()),
                object.getCreatedAt()
        );
    }

}