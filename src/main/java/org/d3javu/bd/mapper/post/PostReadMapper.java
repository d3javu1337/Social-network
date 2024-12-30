package org.d3javu.bd.mapper.post;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.mapper.Mapper;
//import org.d3javu.bd.mapper.user.StaticUserReadMapper;
import org.d3javu.bd.mapper.comment.CommentReadMapper;
import org.d3javu.bd.mapper.user.CompactUserReadMapper;
import org.d3javu.bd.mapper.user.UserReadMapper;
import org.d3javu.bd.models.images.Images;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.models.user.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PostReadMapper implements Mapper<Post, PostReadDto> {

    private final UserReadMapper userReadMapper;
    private final CommentReadMapper commentReadMapper;
    private final CompactUserReadMapper compactUserReadMapper;
//    private final CommentMapper commentMapper;

    @Override
    public PostReadDto map(Post object) {
        return new PostReadDto(
                object.getId(),
                object.getTitle(),
                object.getBody(),
                object.getTags(),
                this.compactUserReadMapper.map(object.getAuthor()),
                Optional.of(object.getViews().stream().map(this.compactUserReadMapper::map).collect(Collectors.toSet()))
                        .orElse(new HashSet<>()),
                Optional.of(object.getLikes().stream().map(User::getId).collect(Collectors.toSet()))
                        .orElse(new HashSet<>()),
//                Optional.of(object.getLikes().stream().map(this.compactUserReadMapper::map).collect(Collectors.toSet()))
//                        .orElse(new HashSet<>()),

//                Optional.of(object.getComments().stream().map(commentReadMapper::map).collect(Collectors.toList()))
//                        .orElse(new ArrayList<>()),
//                new HashSet<>(),
                object.getCreatedAt(),
                object.getImages().stream().map(Images::getPath).collect(Collectors.toList())
        );
    }
}
