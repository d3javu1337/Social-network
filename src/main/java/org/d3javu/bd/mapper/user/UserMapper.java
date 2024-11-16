package org.d3javu.bd.mapper.user;

import org.d3javu.bd.dto.user.UserReadDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.user.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper implements Mapper<User, UserReadDto> {
    @Override
    public UserReadDto map(User object) {
        return new UserReadDto(
                object.getId(),
                object.getFirstName(),
                object.getLastName(),
                object.getEmail(),
                object.getCustomLink(),
                object.getCreatedAt(),
                null
        );
    }

//    @Override
//    public UserReadDto map(User from, UserReadDto to) {
//        return Mapper.super.map(from, to);
//    }
}
