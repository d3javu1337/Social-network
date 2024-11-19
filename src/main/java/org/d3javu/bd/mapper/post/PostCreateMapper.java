package org.d3javu.bd.mapper.post;

import org.d3javu.bd.dto.post.PostCreateDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.images.Images;
import org.d3javu.bd.models.post.Post;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        List<Images> images = new ArrayList<>();
        if(object.getImages() != null) {
            for (var x : object.getImages()) {
                images.add(new Images(post, x.getOriginalFilename()));
            }

            post.setImages(images);
        }else{
            post.setImages(new ArrayList<>());
        }
//        for (var x : object.getImages()) {
//            images.add(new Images(post, x.getOriginalFilename()));
//        }
//
//        post.setImages(Optional.ofNullable(images).orElse(new ArrayList<>()));

        return post;
    }
}