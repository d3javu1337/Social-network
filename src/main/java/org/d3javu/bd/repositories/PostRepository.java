package org.d3javu.bd.repositories;

import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.tag.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long>, FilterPostRepository {

    @Query(value = "select * from posts where id in (select post_id from posts_tags where tag_id = :id)",
            nativeQuery = true)
    List<Post> findAllByTagId(Long id);

    Optional<Post> findById(Long id);


//    @Query(value = "select * from posts where id in (select post_id from post_tags where tag_id in :tags)",
//    nativeQuery = true)
    List<Post> findAllByTags(Set<Tag> tags);

//    List<Post> findAllByOrderByCreatedAtAsc();

    Set<Post> findAllByAuthorId(Long id);

    List<Post> findAllByOrderByLikesCountDesc(Pageable pageable);

    List<Post> findAllByIdIn(Set<Long> ids);

    @Query(value = "select v.id, v.title, v.body, v.created_at, v.likes_count, v.authorid, v.authorfirstname, " +
            "v.authorlastname, v.authoravatarpath from postswithauthorsview v order by v.created_at desc", nativeQuery = true)
    List<Object[]> findAllReadDtoWithAuthor();

    @Query(value = "select v.id, v.title, v.body, v.created_at, v.likes_count, v.authorid, v.authorfirstname, " +
            "v.authorlastname, v.authoravatarpath from postswithauthorsview v where v.id= :id", nativeQuery = true)
    List<Object[]> findAllReadDtoWithAuthorById(Long id);

    @Query(value = "select count(*)>0 from posts_likes pl where pl.post_id= :postId and pl.user_id= :userId",
            nativeQuery = true)
    Boolean existsLikeByPostIdAndUserId(Long postId, Long userId);

    Boolean existsPostById(Long id);

    @Transactional
    @Modifying
    @Query(value = "insert into posts_likes(post_id, user_id) values (:postId, :userId)",
            nativeQuery = true)
    void likePost(Long postId, Long userId);

    @Transactional
    @Modifying
    @Query(value = "delete from posts_likes where post_id= :postId and user_id= :userId",
            nativeQuery = true)
    void unlikePost(Long postId, Long userId);

    @Transactional
    @Modifying
    @Query(value = "update posts set likes_count=(select count(*) from posts_likes where post_id= :postId) where id= :postId",
            nativeQuery = true)
    void updateLikesCount(Long postId);

}
