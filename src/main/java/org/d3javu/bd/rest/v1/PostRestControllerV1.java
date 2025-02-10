package org.d3javu.bd.rest.v1;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.mapper.post.PostReadMapper;
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

//@EnableAsync
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostRestControllerV1 {

    private final PostService postService;
    private final ImageService imageService;
    private final UserService userService;
    private final PostReadMapper postReadMapper;

//    public PostRestController(@Lazy PostService postService, ImageService imageService, UserService userService, PostReadMapper postReadMapper) {
//        this.postService = postService;
//        this.imageService = imageService;
//        this.userService = userService;
//        this.postReadMapper = postReadMapper;
//    }

    //    @Async
    @GetMapping
    public ResponseEntity<List<PostReadDto>> getAllPosts() {
//        System.out.println("request at: %s".formatted(LocalDateTime.now()));
//        var p1 = new PostReadDto(null, null, null, null, null, null, null, LocalDateTime.now(), null);
//        var currUser = Optional.ofNullable(this.getCurrentUser()).orElse(new User());
        var posts = this.postService.findAll(this.getCurrentUserId())
                .stream()
                .sorted(Comparator.comparing(en -> en.createdAt))
                .collect(Collectors.toList());
//        System.out.println("response at: %s".formatted(LocalDateTime.now()));
//        var p2 = new PostReadDto(null, null, null, null, null, null, null, LocalDateTime.now(), null);
//        posts.add(0, p2);
//        posts.add(0, p1);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/images/{imagePath}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public byte[] findImages(@PathVariable Long id, @PathVariable("imagePath") String imagePath) {
        System.out.println(imagePath);
//        var img = this.imageService.getImage(imagePath);
        return this.imageService.getImage(imagePath)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
//        var imgs = this.postService.findImages(id);
//        if (!imgs.isEmpty()){
//            return imgs.get(0);
//        }else{
//            return new byte[0];
//        }
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

//    @GetMapping(value = "/{id}/images", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody List<String> findImages(@PathVariable Long id) {
//        return this.postService.findPostById(id)
//                .map(en -> {
//                    return en.getImages()
//                            .stream().map(Images::getPath)
//                            .collect(Collectors.toList());
//                })
//                .orElse(new ArrayList<String>());
//    }

//    @GetMapping(value = "/{id}/images", produces = MediaType.APPLICATION_JSON_VALUE)
//    public  ResponseEntity<IMGS> findImages(@PathVariable Long id) {
//        var imgs = new IMGS(this.postService.findPostById(id)
//                .map(en -> {
//                    return en.getImages()
//                            .stream().map(Images::getPath)
//                            .collect(Collectors.toList());
//                })
//                .orElse(new ArrayList<String>()));
//        return new ResponseEntity<>(imgs, HttpStatus.OK);
//    }
//
//    record IMGS(List<String> paths){}

//    @GetMapping(value = "/{id}/images", produces = MediaType.IMAGE_PNG_VALUE)
//    public byte[] findImages(@PathVariable Long id) {
//        var imgs = this.postService.findImages(id);
//        if (!imgs.isEmpty()){
//            return imgs.get(0);
//        }else{
//            return new byte[0];
//        }
//    }

//    @RequestMapping(value = "/{id}/images", produces =  MediaType.,
//            method = RequestMethod.GET)
//    public List<byte[]> findImages(@PathVariable Long id) {
//        var imgs = this.postService.findImages(id);
//        if (!imgs.isEmpty()){
//            return imgs;
//        }else{
//            System.out.println("shit happens");
//            return List.of(new byte[0]);
//        }
//    }

}
