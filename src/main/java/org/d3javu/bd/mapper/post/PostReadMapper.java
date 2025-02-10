package org.d3javu.bd.mapper.post;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.mapper.user.CompactUserReadMapper;
import org.d3javu.bd.mapper.user.UserReadMapper;
import org.d3javu.bd.models.post.Post;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostReadMapper implements Mapper<Post, PostReadDto> {

    @Override
    public PostReadDto map(Post object) {
        return new PostReadDto(
//                object.getId(),
//                object.getTitle(),
//                object.getBody(),
//                object.getTags(),
//                this.compactUserReadMapper.map(object.getAuthor()),
//                Optional.of(object.getViews().stream().map(this.compactUserReadMapper::map).collect(Collectors.toSet()))
//                        .orElse(new HashSet<>()),
//                Optional.of(object.getLikes().stream().map(User::getId).collect(Collectors.toSet()))
//                        .orElse(new HashSet<>()),
////                Optional.of(object.getLikes().stream().map(this.compactUserReadMapper::map).collect(Collectors.toSet()))
////                        .orElse(new HashSet<>()),
//
////                Optional.of(object.getComments().stream().map(commentReadMapper::map).collect(Collectors.toList()))
////                        .orElse(new ArrayList<>()),
////                new HashSet<>(),
//                object.getCreatedAt(),
//                object.getImages().stream().map(Images::getPath).collect(Collectors.toList())
        );
    }
}
