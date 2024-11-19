package org.d3javu.bd.models.images;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.d3javu.bd.models.post.Post;

@NoArgsConstructor
@Data
@Entity
@Table(name = "images")
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Post post;

    private String path;

    public Images(Post post, String path) {
        this.post = post;
        this.path = path;
    }

    public Images(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Images{" +
                "id=" + id +
                ", path='" + path + '\'' +
                '}';
    }
}
