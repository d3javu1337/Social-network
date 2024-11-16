package org.d3javu.bd.repositories;

import org.d3javu.bd.models.comment.Comment;
import org.d3javu.bd.models.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select * from comments where post_id = :id",
    nativeQuery = true)
    Set<Comment> findAllByPostId(Long id);

}
