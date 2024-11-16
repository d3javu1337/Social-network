package org.d3javu.bd.dto.comment;

import lombok.Value;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Value
public class CommentReadDto {

    public String body;
    public Post post;
    public User author;
    public Set<User> likes;
    public LocalDateTime createdAt;

}
