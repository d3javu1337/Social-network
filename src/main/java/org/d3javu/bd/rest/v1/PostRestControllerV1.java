package org.d3javu.bd.rest.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.service.ImageService;
import org.d3javu.bd.service.PostService;
import org.d3javu.bd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostRestControllerV1 {

    private final PostService postService;
    private final ImageService imageService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostReadDto>> getAllPosts() {
        log.info("requested all posts");
        var posts = this.postService.findAll(this.getCurrentUserId())
                .stream()
                .sorted(Comparator.comparing(en -> en.createdAt))
                .collect(Collectors.toList());
        log.info("returned posts list: {}", posts.stream().mapToLong(PostReadDto::getId).boxed().collect(Collectors.toList()));
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/images/{imagePath}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public byte[] findImages(@PathVariable Long id, @PathVariable("imagePath") String imagePath) {
        System.out.println(imagePath);
        return this.imageService.getImage(imagePath)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<PostReadDto> like(@PathVariable Long postId) {
        var currentUserId = this.getCurrentUserId();
        this.postService.like(postId, currentUserId);
        var post = this.postService.findPostById(postId, currentUserId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/{postId}/unlike")
    public ResponseEntity<PostReadDto> unlike(@PathVariable Long postId) {
        var currentUserId = this.getCurrentUserId();
        this.postService.unlike(postId, currentUserId);
        var post = this.postService.findPostById(postId, currentUserId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @Deprecated(forRemoval = true)
    public User getCurrentUser(){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findByEmail(userEmail);
    }

    public Long getCurrentUserId(){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findIdByEmail(userEmail);
    }
}
