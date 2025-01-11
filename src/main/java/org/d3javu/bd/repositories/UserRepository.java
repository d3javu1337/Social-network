package org.d3javu.bd.repositories;

import org.d3javu.bd.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long>, FilterUserRepository, QuerydslPredicateExecutor<User> {

    @Query(value = "select * from users where id in (select f.followed_id from follows f where f.follower_id = :id)",
            nativeQuery = true)
    List<User> findFollowsById(Long id);

    Optional<User> findByEmail(String username);

    @Query(value = "select u.id from users u where u.email= :email",
            nativeQuery = true)
    Optional<Long> findIdByEmail(String email);

    Optional<User> findByCustomLink(String username);

    @Query(value = "select u.id, u.first_name, u.last_name, u.avatar_path from users u where u.id= :id",
            nativeQuery = true)
    List<Object[]> findCompactById(Long id);

    @Query(value = "select u.first_name, u.last_name, u.avatar_path from users u where u.id= :id", nativeQuery = true)
    String[] FindFirstNameAndLastNameAndAvatarPathById(Long id);

    @Query(value = "select count(*)>0 from follows where followed_id= :followedId and follower_id= :followerId",
            nativeQuery = true)
    Boolean existsFollow(Long followedId, Long followerId);

    @Query(value = "select u.id, u.first_name, u.last_name, u.avatar_path from users u where u.id in " +
            "(select f.follower_id from follows f where followed_id= :followedId)",
            nativeQuery = true)
    List<Object[]> findAllFollowersByFollowedId(Long followedId);

    @Query(value = "select pt.tag_id from user_preferred_tags pt where user_id= :userId",
            nativeQuery = true)
    Set<Long> findAllPreferredTagsIdByUserId(Long userId);

    @Query(value = "select count(*) from follows where followed_id= :userId",
            nativeQuery = true)
    Long getFollowersCountByUserId(Long userId);

    @Query(value = "select count(*) from follows where follower_id= :userId",
            nativeQuery = true)
    Long getFollowsCountByUserId(Long userId);

}
