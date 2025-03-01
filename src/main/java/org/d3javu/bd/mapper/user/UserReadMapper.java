package org.d3javu.bd.mapper.user;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.dto.user.UserReadDto;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final UserMapper userMapper;

    @Override
    public UserReadDto map(User object) {
        return new UserReadDto(
                object.getId(),
                object.getFirstName(),
                object.getLastName(),
                object.getEmail(),
                object.getCustomLink(),
                object.getCreatedAt(),
                object.getFollowers().stream().map(userMapper::map).collect(Collectors.toList()),
                object.getAvatarPath()
                );
    }

//    @Override
//    public UserReadDto map(User from, UserReadDto to) {
//        to.setId(from.getId());
//        to.setFirstName(from.getFirstName());
//        to.setLastName(from.getLastName());
//        to.setUsername(from.getEmail());
//        to.setCustomLink(from.getCustomLink());
//        to.setCreatedAt(from.getCreatedAt());
//        to.setFollowers(from.getFollowers().stream().map(userMapper::map).collect(Collectors.toSet()));
//        to.setAvatar(from.getAvatarPath());
//        return to;
//    }
}
