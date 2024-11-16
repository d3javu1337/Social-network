package org.d3javu.bd.mapper.comment;

import org.d3javu.bd.dto.comment.CommentEditDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.comment.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentEditMapper implements Mapper<CommentEditDto, Comment> {

    @Override
    public Comment map(CommentEditDto object) {
        Comment comment = new Comment();
        comment.setBody(object.getBody());
        return comment;
    }

    @Override
    public Comment map(CommentEditDto from, Comment to) {
        to.setBody(from.getBody());
        return to;
    }

}
