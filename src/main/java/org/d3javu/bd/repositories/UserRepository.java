package org.d3javu.bd.repositories;

import org.d3javu.bd.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, FilterUserRepository, QuerydslPredicateExecutor<User> {

    @Query(value = "select * from users where id in (select f.follower_id from follows f where f.followed_id = :id)",
            nativeQuery = true)
    List<User> findFollowersById(Long id);

    Optional<User> findByEmail(String username);

}
