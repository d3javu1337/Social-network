package org.d3javu.bd.repositories;

import org.d3javu.bd.models.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByBody(String name);

    @Query(value = "select * from tags where id in (select tag_id from posts_tags where post_id = :postId)",
            nativeQuery = true)
    Set<Tag> findTagsByPostId(Long postId);

    @Query(value = "select t.id, t.body, t.description from tags t where t.id in(:tagsIds)",
            nativeQuery = true)
    Set<Object[]> findTagsByIds(Set<Long> tagsIds);

}
