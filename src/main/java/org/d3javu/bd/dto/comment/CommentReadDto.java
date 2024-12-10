package org.d3javu.bd.dto.comment;

import lombok.Value;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.dto.user.CompactUserReadDto;
import org.d3javu.bd.dto.user.UserReadDto;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Value
public class CommentReadDto {

    public Long id;
    public String body;
    public PostReadDto post;
    public CompactUserReadDto author;
    public Set<CompactUserReadDto> likes;
    public LocalDateTime createdAt;

}
