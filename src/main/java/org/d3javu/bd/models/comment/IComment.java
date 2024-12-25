package org.d3javu.bd.models.comment;

import org.d3javu.bd.models.user.IUser;

import java.time.LocalDateTime;
import java.util.Set;

public interface IComment {

    Long getId();
    String getBody();
    LocalDateTime getCreatedAt();
    IUser getAuthor();
    Set<Long> getLikes();
}
