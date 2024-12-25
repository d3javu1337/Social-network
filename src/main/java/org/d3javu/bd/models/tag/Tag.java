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

    public Tag(String body, String description) {
        this.body = body;
        this.description = description;
    }

    public Tag(String body) {
        this.body = body;
        this.description = "";
    }

}
