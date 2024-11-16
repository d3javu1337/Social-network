package org.d3javu.bd.mapper.comment;

import org.d3javu.bd.dto.comment.CommentCreateDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.comment.Comment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentCreateMapper implements Mapper<CommentCreateDto, Comment> {


    @Override
    public Comment map(CommentCreateDto object) {
        Comment comment = new Comment();
        comment.setBody(object.getBody());
        comment.setUser(object.getUser());
        comment.setCreatedAt(LocalDateTime.now());
        return comment;
    }
}
