package org.d3javu.bd.repositories;

import org.d3javu.bd.models.comment.Comment;
import org.d3javu.bd.models.comment.IComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

//    @Query(value = "select c.id, c.body, c.created_at, c.user_id from comments c where id= :id", nativeQuery = true)
    @Query(value = "select " +
            "cv.id, cv.body, cv.created_at, cv.likes_count, cv.post_id, " +
            "cv.authorid, cv.authorfirstname, cv.authorlastname, cv.authoravatarpath " +
            "from commentswithauthorsview cv where id= :id ", nativeQuery = true)
    List<Object[]> getIdAndBodyAndCreatedAtAndAuthorIdByCommentId(Long id);

    @Query(value = "select cl.user_id from comments_likes cl where comment_id= :id", nativeQuery = true)
    Set<Long> getLikesIdsById(Long id);

    @Query(value = "select count(*) from comments_likes where comment_id= :id",
            nativeQuery = true)
    Long getLikesCountById(Long id);

    @Query(value = "select count(*)>0 from comments_likes where user_id= :userId and comment_id= :commentId",
            nativeQuery = true)
    Boolean existsLikeByUserIdAndCommentId(Long userId, Long commentId);

    @Transactional
//    @Modifying
    @Query(value = "insert into comments(created_at, likes_count, post_id, user_id, body) " +
            "values (:createdAt, :likesCount, :postId, :userId, :body) returning id",
            nativeQuery = true)
    Long createComment(String body, Long postId, Long userId, LocalDateTime createdAt, Long likesCount);

    @Transactional
    @Modifying
    @Query(value = "insert into comments_likes(comment_id, user_id) values (:commentId, :userId)",
        nativeQuery = true)
    void likeComment(Long userId, Long commentId);

    @Transactional
    @Modifying
    @Query(value = "delete from comments_likes where comment_id= :commentId and user_id= :userId",
        nativeQuery = true)
    void unlikeComment(Long userId, Long commentId);

    @Transactional
    @Modifying
    @Query(value = "update comments c set likes_count = " +
            "(select count(*) from comments_likes cl where cl.comment_id= :commentId) " +
            "where c.id= :commentId",
            nativeQuery = true)
    void updateLikesCountByCommentId(Long commentId);

    @Transactional
    @Modifying
    @Query(value = "delete from comments where id= :id",
            nativeQuery = true)
    void deleteCommentById(Long id);

    @Transactional
    @Modifying
    @Query(value = "update comments set body= :newBody where id= :commentId",
            nativeQuery = true)
    void updateComment(Long commentId, String newBody);

    @Query(value = "select id, body, created_at, post_id, authorId as userId, authorFirstName as userFirstName," +
            "authorLastName as userLastName, authorAvatarPath as userAvatarPath, likes_count from commentsWithAuthorsView " +
            "where post_id= :postId",
            nativeQuery = true)
    Set<IComment> findAllDtoByPostId(Long postId);

    @Query(value = "select id, body, created_at, post_id, authorId as userId, authorFirstName as userFirstName, " +
            "authorLastName as userLastName, authorAvatarPath as userAvatarPath, likes_count from commentsWithAuthorsView " +
            "where id= :commentId",
            nativeQuery = true)
    Optional<IComment> findCommentById(Long commentId);
}
