package org.d3javu.bd.models.tag;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false, unique = true)
    private String body;

    @Column(nullable = false)
    private String description;

//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "Post")
//    @JoinTable(name = "post_tags")
//    private List<Post> posts;

    public Tag(String body, String description) {
        this.body = body;
        this.description = description;
    }

    public Tag(String body) {
        this.body = body;
        this.description = "";
    }

//    public void linkPost(Post post) {
//        if (this.posts == null) this.posts = new ArrayList<>();
//        this.posts.add(post);
//    }
}
