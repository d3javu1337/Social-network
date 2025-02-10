package org.d3javu.bd.mapper.comment;

import org.d3javu.bd.dto.comment.CommentReadDto;
import org.d3javu.bd.dto.user.CompactUserReadDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.comment.IComment;
import org.springframework.stereotype.Component;

@Component
public class CommentReadMapper implements Mapper<IComment, CommentReadDto> {

    @Override
    public CommentReadDto map(IComment object) {
        System.out.println(object.getId());
        return new CommentReadDto(
                object.getId(),
                object.getBody(),
                object.getCreatedAt(),
                object.getPostId(),
                new CompactUserReadDto(object.getUserId(), object.getUserFirstName(), object.getUserLastName(), object.getUserAvatarPath()),
                object.getLikesCount()
        );
    }

}