package org.d3javu.bd.models.post;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.d3javu.bd.models.comment.Comment;
import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.models.user.User;

import java.time.LocalDateTime;
import java.util.*;


@NoArgsConstructor
@Data
//@EqualsAndHashCode
@ToString
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String body;

    @ManyToOne
    private User author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="posts_views",
            joinColumns=@JoinColumn(name="post_id"),
            inverseJoinColumns=@JoinColumn(name="user_id")
    )
    private Set<User> views;

    private Long viewsCount = 0L;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="posts_likes",
            joinColumns=@JoinColumn(name="post_id"),
            inverseJoinColumns=@JoinColumn(name="user_id")
    )
    private Set<User> likes;

    private Long likesCount = 0L;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "post")
    private Set<Comment> comments;

    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(name="posts_tags",
            joinColumns=@JoinColumn(name="post_id"),
            inverseJoinColumns=@JoinColumn(name="tag_id")
    )
    private Set<Tag> tags;

    public Post(String title, String body, Set<Tag> tags) {
        this.title = title;
        this.body = body;
        this.tags = tags;
//        tags.forEach(e -> e.linkPost(this));
        this.createdAt = LocalDateTime.now();
    }

    public void linkUser(User author){
        this.author = author;
    }

    public void addComment(Comment comment){
        if(this.comments == null) this.comments = new HashSet<>();
        this.comments.add(comment);
    }

    public void like(User user){
        if(this.likes == null) this.likes = new HashSet<>();
        this.likes.add(user);
        this.likesCount = (long) this.likes.size();
        this.view(user);
    }

    public void unlike(User user){
        if (this.likes != null) {
            this.likes.remove(user);
            this.likesCount = (long) this.likes.size();
        }
    }

    public void view(User user){
        if(this.views == null) this.views = new HashSet<>();
        this.views.add(user);
        this.viewsCount = (long) this.views.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(title, post.title) && Objects.equals(body, post.body) && Objects.equals(author, post.author) && Objects.equals(createdAt, post.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, body, author, createdAt);
    }
}
