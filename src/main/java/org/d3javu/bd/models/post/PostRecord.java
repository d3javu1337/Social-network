package org.d3javu.bd.models.post;

import jakarta.persistence.Id;

import java.time.LocalDateTime;

//@Entity
public record PostRecord(
        @Id Long id,
        LocalDateTime createdAt,
        Long authorId,
        Long likesCount,
        Long viewsCount,
        String body,
        String title
){
//    @Override
//    public String toString() {
//        return "PostRecord{" +
//                "id=" + id +
//                ", createdAt=" + createdAt +
//                ", authorId=" + authorId +
//                ", likesCount=" + likesCount +
//                ", viewsCount=" + viewsCount +
//                ", body='" + body + '\'' +
//                ", title='" + title + '\'' +
//                '}';
//    }
}
