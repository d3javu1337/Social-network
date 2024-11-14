package org.d3javu.bd.dto.post;

import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.models.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PostReadDto {

    public String title;
    public String content;
    public Set<Tag> tags = new HashSet<>();
    public User author;
//    public Long viewsCount;
    public Set<User> views = new HashSet<>();
    public Set<User> likes = new HashSet<>();
    public Set<User> comments = new HashSet<>();
    public LocalDateTime createdAt;

}
