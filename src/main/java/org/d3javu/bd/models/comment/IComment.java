package org.d3javu.bd.models.comment;

import org.d3javu.bd.models.user.IUser;

import java.time.LocalDateTime;
import java.util.Set;

public interface IComment {

    Long getId();
    String getBody();
    LocalDateTime getCreatedAt();
    Long getPostId();
    Long getUserId();
    String getUserFirstName();
    String getUserLastName();
    String getUserAvatarPath();
    Long getLikesCount();
}
