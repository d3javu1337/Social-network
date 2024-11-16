package org.d3javu.bd.mapper.post;

import org.d3javu.bd.dto.post.PostEditDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.tag.Tag;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class PostEditMapper implements Mapper<PostEditDto, Post> {
    @Override
    public Post map(PostEditDto object) {
        Post post = new Post();
        post.setTitle(object.getTitle());
        post.setBody(object.getBody());
//        post.setTags(object.getTags());
        post.setTags(Optional.ofNullable(object.getTags()).orElse(new HashSet<>()));
        return post;
    }

    @Override
    public Post map(PostEditDto from, Post to) {
        to.setTitle(from.getTitle());
        to.setBody(from.getBody());
//        to.setTags(from.getTags());
        to.setTags(Optional.ofNullable(from.getTags()).orElse(new HashSet<>()));
        return to;
    }
}