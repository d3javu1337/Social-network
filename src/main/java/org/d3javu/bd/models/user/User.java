package org.d3javu.bd.models.user;

import jakarta.persistence.*;
import lombok.*;
//import org.d3javu.bd.auth.authData.AuthData;
import org.d3javu.bd.models.comment.Comment;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.tag.Tag;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
//@NoArgsConstructor
@Data
//@EqualsAndHashCode
//@ToString
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String customLink;
    private LocalDateTime createdAt;

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private AuthData authData;

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="follows",
            joinColumns=@JoinColumn(name="follower_id"),
            inverseJoinColumns=@JoinColumn(name="followed_id")
    )
    private Set<User> follows;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name="follows",
            joinColumns=@JoinColumn(name="followed_id"),
            inverseJoinColumns=@JoinColumn(name="follower_id")
    )
    private Set<User> followers;

//    @OneToMany(cascade = CascadeType.ALL)
//    private List<Tag> preferredTags;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_preferred_tags",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="tag_id")
    )
    private Set<Tag> preferredTags;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<Post> posts;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
//        this.customLink = customLink;
        this.createdAt = LocalDateTime.now();
//        this.roles = new ArrayList<>();
//        this.roles.add(Roles.user);
        this.role = Roles.admin;
        this.password = "{noop}"+password;
    }

    public User(){
        this.createdAt = LocalDateTime.now();
    }

//    public boolean addPost(Post post) {
//        if (this.posts == null) this.posts = new ArrayList<>();
//        post.linkUser(this);
//        return this.posts.add(post);
//    }

    public void addComment(String text, Post post) {
        var comm = new Comment(text, post, this);
    }

    public void follow(User user) {
        if (follows == null) this.follows = new HashSet<>();
        follows.add(user);
//        if (user.getFollowers() == null) user.setFollowers(new HashSet<>());
//        user.getFollowers().add(this);

        // не нужна тебе такая машина, вовка
        // нужна мне такая машина, только как сделать хуй его знает
        // eom
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(customLink, user.customLink) && Objects.equals(createdAt, user.createdAt) && Objects.equals(follows, user.follows) && Objects.equals(followers, user.followers) && Objects.equals(preferredTags, user.preferredTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, createdAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", customLink='" + customLink + '\'' +
                ", createdAt=" + createdAt +
//                ", follows=" + follows +
//                ", followers=" + followers +
                ", preferredTags=" + preferredTags +
                '}';
    }
}
