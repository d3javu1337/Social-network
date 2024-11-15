package org.d3javu.bd.mapper.post;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.mapper.user.StaticUserReadMapper;
import org.d3javu.bd.mapper.user.UserReadMapper;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.models.user.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PostReadMapper implements Mapper<Post, PostReadDto> {

    private final UserReadMapper userReadMapper;
//    private final CommentMapper commentMapper;

    @Override
    public PostReadDto map(Post object) {
        return new PostReadDto(
                object.getId(),
                object.getTitle(),
                object.getBody(),
                object.getTags(),
                StaticUserReadMapper.map(object.getAuthor()),
                Optional.of(object.getViews().stream().map(userReadMapper::map).collect(Collectors.toSet())).orElse(new HashSet<>()),
                Optional.of(object.getLikes().stream().map(userReadMapper::map).collect(Collectors.toSet())).orElse(new HashSet<>()),
//                object.getComments().stream().map(commentM).collect(Collectors.toSet()),
                new HashSet<>(),
                object.getCreatedAt()
        );
    }
}
