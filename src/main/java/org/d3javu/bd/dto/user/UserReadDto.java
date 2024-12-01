package org.d3javu.bd.dto.user;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Value
public class UserReadDto {

    Long id;
    String firstName;
    String lastName;
    String username;
    String customLink;
    LocalDateTime createdAt;
    List<UserReadDto> followers;
    String avatar;


}
