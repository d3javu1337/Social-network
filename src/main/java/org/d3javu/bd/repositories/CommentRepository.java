package org.d3javu.bd.repositories;

import org.d3javu.bd.dto.comment.CommentDtoForLargeQuery;
import org.d3javu.bd.dto.comment.CommentReadDto;
import org.d3javu.bd.models.comment.Comment;
import org.d3javu.bd.models.comment.IComment;
import org.d3javu.bd.models.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

//    @Query(value = "select * from comments where post_id = :id",
//    nativeQuery = true)
//    Set<CommentReadDto> findAllByPostId(Long id);
//    @Query(value = "select\n" +
//            "c.id, c.body, c.created_at, u.id, u.first_name, u.last_name, u.avatar_path,\n" +
//            "ul.id, ul.first_name, ul.last_name, ul.avatar_path\n" +
//            "from comments c join users u on c.user_id=u.id\n" +
//            "join comments_likes cl on cl.comment_id=c.id\n" +
//            "join users ul on ul.id=cl.user_id\n" +
//            "join posts p on c.post_id = p.id\n" +
//            "where p.id= :id",
//    nativeQuery = true)
//    Set<IComment> findAllByPostId(Long id);

    @Query(value = "select c.id, c.body, c.created_at, c.user_id from comments c where post_id= :id", nativeQuery = true)
    List<Object[]> getIdAndBodyAndCreatedAtAndAuthorIdByPostId (Long id);

    @Query(value = "select cl.user_id from comments_likes cl where comment_id= :id", nativeQuery = true)
    Set<Long> getLikesIdsById(Long id);
}
