package org.d3javu.bd.repositories;

import org.d3javu.bd.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, FilterUserRepository, QuerydslPredicateExecutor<User> {

    @Query(value = "select * from users where id in (select f.followed_id from follows f where f.follower_id = :id)",
            nativeQuery = true)
    List<User> findFollowsById(Long id);

    Optional<User> findByEmail(String username);

    Optional<User> findByCustomLink(String username);

}
