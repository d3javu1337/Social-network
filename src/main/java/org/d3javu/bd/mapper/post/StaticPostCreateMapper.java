package org.d3javu.bd.mapper.post;

import org.d3javu.bd.dto.post.PostCreateDto;
import org.d3javu.bd.models.post.Post;

import java.time.LocalDateTime;

public class StaticPostCreateMapper {

    public static Post map(PostCreateDto object) {
        Post post = new Post();
        post.setTitle(object.getTitle());
        post.setBody(object.getBody());
        post.setTags(object.getTags());
        post.setCreatedAt(LocalDateTime.now());
        return post;
    }

}
