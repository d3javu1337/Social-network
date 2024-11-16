package org.d3javu.bd.dto.user;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class UserReadDto {

    Long id;
    String firstName;
    String lastName;
    String username;
    String customLink;
    LocalDateTime createdAt;


}
