package org.d3javu.bd.dto.user;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

@Value
public class UserReadDto {

    Long id;
    String firstName;
    String lastName;
    String username;
    String customLink;
    LocalDateTime createdAt;
    Set<UserReadDto> followers;


}
