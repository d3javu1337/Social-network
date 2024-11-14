package org.d3javu.bd.mapper.user;

import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.dto.user.UserEditDto;
import org.springframework.stereotype.Component;

@Component
public class UserEditMapper implements Mapper<UserEditDto, User> {

    @Override
    public User map(UserEditDto object) {
        User user = new User();
        user.setFirstName(object.getFirstName());
        user.setLastName(object.getLastName());
        user.setEmail(object.getUsername());
        user.setCustomLink(object.getCustomLink());

        return user;
    }

    public User map(UserEditDto object, User user) {
        user.setFirstName(object.getFirstName());
        user.setLastName(object.getLastName());
        user.setEmail(object.getUsername());
        user.setCustomLink(object.getCustomLink());
        return user;
    }

}
