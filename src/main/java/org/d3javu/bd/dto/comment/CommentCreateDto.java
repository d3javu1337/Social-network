package org.d3javu.bd.dto.comment;

import lombok.Value;
import org.d3javu.bd.models.user.User;

@Value
public class CommentCreateDto {

    public String body;
    public User user;


}
