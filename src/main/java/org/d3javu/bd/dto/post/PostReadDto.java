package org.d3javu.bd.dto.post;

import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import org.d3javu.bd.dto.tag.TagDto;
import org.d3javu.bd.dto.user.CompactUserReadDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;



@QueryEntity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostReadDto {

    public Long id;
    public String title;
    public String body;
    public Set<TagDto> tags;
    public CompactUserReadDto author;
    public Boolean isLiked;
    public Long likesCount;
    public LocalDateTime createdAt;
    public List<String> images;

    public PostReadDto(Long id, String title, String body, LocalDateTime createdAt, Long likesCount, Long authorId,
                         String authorFirstName, String authorLastName, String authorAvatarPath) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.likesCount = likesCount;
        this.author = new CompactUserReadDto(authorId, authorFirstName, authorLastName, authorAvatarPath);
    }
}
