package org.d3javu.bd.repositories;

import org.d3javu.bd.models.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
