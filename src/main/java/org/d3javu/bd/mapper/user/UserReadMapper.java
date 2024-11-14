package org.d3javu.bd.mapper.user;

import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.dto.user.UserReadDto;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {
    @Override
    public UserReadDto map(User object) {
        return new UserReadDto(
                object.getId(),
                object.getFirstName(),
                object.getLastName(),
                object.getEmail(),
                object.getCustomLink(),
                object.getCreatedAt()
                );
    }


}
