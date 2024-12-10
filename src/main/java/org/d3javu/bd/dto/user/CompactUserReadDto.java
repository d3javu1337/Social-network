package org.d3javu.bd.dto.user;

import lombok.Value;

@Value
public class CompactUserReadDto {

    Long id;
    String firstName;
    String lastName;
    String avatar;

}
