package org.d3javu.bd.models.post;

import java.time.LocalDateTime;

public interface IPost {

    Long getId();
    LocalDateTime getCreatedAt();
    Long getAuthorId();
    Long getLikesCount();
    Long getViewsCount();
    String getBody();
    String getTitle();

}
