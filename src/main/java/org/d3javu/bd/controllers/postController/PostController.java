package org.d3javu.bd.controllers.postController;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.post.PostEditDto;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.dto.user.UserReadDto;
import org.d3javu.bd.mapper.post.PostEditMapper;
import org.d3javu.bd.mapper.post.PostReadMapper;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.service.PostService;
import org.d3javu.bd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/posts")
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final PostEditMapper postEditMapper;
    private final PostReadMapper postReadMapper;

//    @PostMapping("/create")
//    public String create(){
//        return "/posts/create";
//    }

    @GetMapping("/preferred")
    public String findAllPreferred(Model model) {
        var user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var tags = user.getPreferredTags();
        List<PostReadDto> posts = this.postService.findByPreferred(tags);
        model.addAttribute("posts", posts);
//        return "/post/preferred";
        return "/post/posts";
    }

    @GetMapping
    public String findAll(Model model) {
//        this.postService.findAll();
        model.addAttribute("posts", this.postService.findAll());
        return "/post/posts";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model){
//        System.out.println(postService.findById(id));
//        model.addAttribute("post", postService.findById(id));
        var userId = this.userService.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName()).getId();
//        System.out.println(user + "----------------------------------------------");
//        var user1 = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

//        System.out.println(user1);
//        var userId = user.getId();
//        System.out.println(user+"--------------------------------------");
//        model.addAttribute("user", user);
        return postService.findById(id)
                .map(post -> {
                    model.addAttribute("post", post);
                    model.addAttribute("userId", userId);
                    model.addAttribute("authorId", post.getAuthor().getId());
                    return "/post/post";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        return "/post/post";
    }

    @GetMapping("/{id}/update")
    public String update(Model model, @PathVariable Long id){
        var post = this.postService.findById(id).get();
        System.out.println(post+"-----------------------------------------");
        model.addAttribute("post", post);
        return "/post/postUpdate";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, PostEditDto post){
//        return postService.update(id, post).map()
        return postService.update(id, post)
                .map(it -> "redirect:/posts/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

//    @PostMapping("/{id}/delete")
//    public String delete(){}

}