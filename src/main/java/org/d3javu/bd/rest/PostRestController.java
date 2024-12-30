package org.d3javu.bd.rest;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

//@EnableAsync
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostRestController {

    private final PostService postService;
    private final ImageService imageService;
    private final UserService userService;
    private final PostReadMapper postReadMapper;

//    @Async
    @GetMapping
    public CompletableFuture<ResponseEntity<List<PostReadDto>>> getAllPosts() {
        return CompletableFuture.completedFuture(new ResponseEntity<>(this.postService.findAll(), HttpStatus.OK));
    }

    @GetMapping(value = "/{id}/images/{imagePath}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] findImages(@PathVariable Long id, @PathVariable("imagePath") String imagePath) {
        System.out.println(imagePath);
        var img = this.imageService.getImage(imagePath);
        return img.get();
//        var imgs = this.postService.findImages(id);
//        if (!imgs.isEmpty()){
//            return imgs.get(0);
//        }else{
//            return new byte[0];
//        }
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<PostReadDto> like(@PathVariable Long postId) {
//        this.postService.findPostById(postId)
//                .map(en ->{
//                    en.like(this.getCurrentUser());
//                    this.postService.up
//                    return new ResponseEntity<>(en, HttpStatus.OK);
//                });
        this.postService.like(postId, this.getCurrentUser());
        var post = this.postService.findPostById(postId)
                .map(this.postReadMapper::map)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/{postId}/unlike")
    public ResponseEntity<PostReadDto> unlike(@PathVariable Long postId) {
        this.postService.unlike(postId, this.getCurrentUser());
        var post = this.postService.findPostById(postId)
                .map(this.postReadMapper::map)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    public User getCurrentUser(){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findByEmail(userEmail);
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
