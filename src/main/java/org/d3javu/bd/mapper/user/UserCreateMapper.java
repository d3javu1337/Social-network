package org.d3javu.bd.mapper.user;

import org.d3javu.bd.dto.user.UserCreateDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.user.Roles;
import org.d3javu.bd.models.user.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Component
public class UserCreateMapper implements Mapper<UserCreateDto, User> {
    @Override
    public User map(UserCreateDto object) {
        User user = new User();
        user.setFirstName(object.firstName);
        user.setLastName(object.lastName);
        user.setEmail(object.login);
        user.setPassword("{noop}"+object.password);
        user.setRole(Roles.user);
        user.setPreferredTags(object.preferredTags);
        user.setPreferredTags(Optional.ofNullable(object.preferredTags).orElse(new HashSet<>()));
        return user;
    }

//    @Override
//    public User map(UserCreateDto from, User to) {
//        to.setFirstName(from.firstName);
//        to.setLastName(from.lastName);
//        to.setEmail(from.login);
//        to.setPassword(from.password);
//        to.setPreferredTags(from.preferredTags);
//        return to;
//    }
}

/*
    public String firstName;
    public String lastName;
    public String username;
    public String customLink;
    public String password;
    public Set<Tag> tags = new HashSet<>();


 */