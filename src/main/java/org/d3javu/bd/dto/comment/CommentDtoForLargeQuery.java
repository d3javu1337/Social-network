package org.d3javu.bd.dto.comment;

import org.d3javu.bd.dto.user.CompactUserReadDto;
import org.d3javu.bd.models.user.IUser;

import java.time.LocalDateTime;
import java.util.Set;

public class CommentDtoForLargeQuery {

    public Long id;
    public String body;
    public LocalDateTime createdAt;
    public Long postId;
    public CompactUserReadDto author;
    public Set<Long> likes;
//


    public CommentDtoForLargeQuery(Long id, String body, LocalDateTime createdAt, Long postId, CompactUserReadDto author, Set<Long> likes) {
        this.id = id;
        this.body = body;
        this.createdAt = createdAt;
        this.postId = postId;
        this.author = author;
        this.likes = likes;
    }
}
