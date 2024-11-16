package org.d3javu.bd.mapper.user;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.user.UserReadDto;
import org.d3javu.bd.models.user.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
//
//@RequiredArgsConstructor
//@Component
//public class StaticUserReadMapper {
//
//
//
//    public static UserReadDto map(User object) {
//        return new UserReadDto(
//                object.getId(),
//                object.getFirstName(),
//                object.getLastName(),
//                object.getEmail(),
//                object.getCustomLink(),
//                object.getCreatedAt(),
//                Optional.ofNullable(object.getFollowers()).map(this::).orElse(new HashSet<>())
//        );
//    }
//
//
//}
