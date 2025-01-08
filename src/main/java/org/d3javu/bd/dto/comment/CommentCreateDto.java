package org.d3javu.bd.dto.comment;

import lombok.Data;
import lombok.Value;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.user.User;

import java.time.LocalDateTime;

@Data
public class CommentCreateDto {

    public String body;
    public Long userId;
    public Long postId;
    public LocalDateTime createdAt;
    public Long likesCount;

}
