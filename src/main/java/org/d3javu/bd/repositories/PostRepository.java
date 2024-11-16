package org.d3javu.bd.repositories;

import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    List<Post> findAllByOrderByCreatedAtAsc();

}
