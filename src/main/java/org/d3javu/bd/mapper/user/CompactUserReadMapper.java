package org.d3javu.bd.mapper.user;

import org.d3javu.bd.dto.user.CompactUserReadDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.user.User;
import org.springframework.stereotype.Component;

@Component
public class CompactUserReadMapper implements Mapper<User, CompactUserReadDto> {
    @Override
    public CompactUserReadDto map(User object) {

        return new CompactUserReadDto(
          object.getId(),
          object.getFirstName(),
          object.getLastName(),
          object.getAvatarPath()
        );
    }
}
