package org.d3javu.bd.mapper.post;

import org.d3javu.bd.dto.post.PostCreateDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.post.Post;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostCreateMapper implements Mapper<PostCreateDto, Post> {

    @Override
    public Post map(PostCreateDto object) {
        Post post = new Post();
        post.setTitle(object.getTitle());
        post.setBody(object.getBody());
        post.setAuthor(object.getAuthor());
        post.setTags(object.getTags());
        post.setCreatedAt(LocalDateTime.now());
        return post;
    }

//    @Override
//    public Post map(PostCreateDto from, Post to) {
//        return to;
//    }
}
