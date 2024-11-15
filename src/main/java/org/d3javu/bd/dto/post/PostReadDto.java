package org.d3javu.bd.dto.post;

import lombok.Value;
import org.d3javu.bd.dto.user.UserReadDto;
import org.d3javu.bd.models.comment.Comment;
import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.models.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Value
public class PostReadDto {

    public Long id;
    public String title;
    public String body;
    public Set<Tag> tags;
    public UserReadDto author;
//    public Long viewsCount;
    public Set<User> views;
    public Set<User> likes;
    public Set<Comment> comments;
    public LocalDateTime createdAt;

}
