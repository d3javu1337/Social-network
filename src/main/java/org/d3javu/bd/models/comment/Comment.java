package org.d3javu.bd.models.comment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.models.post.Post;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Data
//@EqualsAndHashCode
//@ToString
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String body;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(name="comments_likes",
            joinColumns=@JoinColumn(name="comment_id"),
            inverseJoinColumns=@JoinColumn(name="user_id")
    )
    private Set<User> likes = new HashSet<>();

    private Long likesCount = 0L;

    private LocalDateTime createdAt;

    public Comment(String body, Post post, User user) {
        this.body = body;
        this.post = post;
        post.addComment(this);
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

//    public Comment(){
//        this.createdAt = LocalDateTime.now();
//    }

    public void like(User user) {
        if (this.likes == null) this.likes = new HashSet<>();
        this.likes.add(user);
//        this.likesCount = (long) this.likes.size();
    }

    public void unlike(User user) {
        if (this.likes != null){
            this.likes.remove(user);
//            this.likesCount = (long) this.likes.size();
        }
    }

    @Override
    public String toString() {
        return "Comment{" +
//                "likesCount=" + likesCount +
                ", user=" + user +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(body, comment.body) && Objects.equals(post, comment.post) && Objects.equals(user, comment.user) && Objects.equals(createdAt, comment.createdAt);
    }

    @Override
    public int hashCode() {
//        return Objects.hash(id, body, post, user, createdAt);
        return Objects.hash(id, createdAt);
    }
}
