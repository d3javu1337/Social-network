package org.d3javu.bd.dto.comment;

import lombok.Data;
import lombok.Value;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.user.User;

@Data
public class CommentCreateDto {

    public String body;
    public User user;
    public Post post;

}
