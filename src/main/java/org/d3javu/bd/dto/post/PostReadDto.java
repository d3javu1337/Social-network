package org.d3javu.bd.dto.post;

import lombok.Value;
import org.d3javu.bd.dto.comment.CommentReadDto;
import org.d3javu.bd.dto.user.UserReadDto;
import org.d3javu.bd.models.comment.Comment;
import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.models.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Value
public class PostReadDto {

    public Long id;
    public String title;
    public String body;
    public Set<Tag> tags;
    public UserReadDto author;
//    public Long viewsCount;
    public Set<UserReadDto> views;
    public Set<UserReadDto> likes;
//    public List<CommentReadDto> comments;
    public LocalDateTime createdAt;
    public List<String> images;

}
