package org.d3javu.bd.dto.comment;

import lombok.NoArgsConstructor;
import org.d3javu.bd.dto.user.CompactUserReadDto;

import java.time.LocalDateTime;

@NoArgsConstructor
public class CommentReadDto {

    public Long id;
    public String body;
    public LocalDateTime createdAt;
    public Long postId;
    public CompactUserReadDto author;
    public Long likesCount;
    public Boolean isLiked;
//


    public CommentReadDto(Long id, String body, LocalDateTime createdAt, Long postId, CompactUserReadDto author, Long likesCount, Boolean isLiked) {
        this.id = id;
        this.body = body;
        this.createdAt = createdAt;
        this.postId = postId;
        this.author = author;
        this.likesCount = likesCount;
        this.isLiked = isLiked;
    }

    public CommentReadDto(Long id, String body, LocalDateTime createdAt, Long postId, CompactUserReadDto author, Long likesCount) {
        this.id = id;
        this.body = body;
        this.createdAt = createdAt;
        this.postId = postId;
        this.author = author;
        this.likesCount = likesCount;
    }
}
