package org.d3javu.bd.repositories;

import org.d3javu.bd.filter.user.UserFilter;
import org.d3javu.bd.models.user.User;

import java.util.List;

public interface FilterUserRepository {

    List<User> findAllByFilter(UserFilter filter);

}
