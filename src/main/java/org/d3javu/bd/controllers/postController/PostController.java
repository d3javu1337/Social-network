package org.d3javu.bd.controllers.postController;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/posts")
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

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
        return postService.findById(id)
                .map(post -> {
                    model.addAttribute("post", post);
                    return "/post/post";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        return "/post/post";
    }

    @PostMapping("/{id}/update")
    public String update(Model model, @PathVariable Long id, PostReadDto post){
        var user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "";
    }

//    @PostMapping("/{id}/delete")
//    public String delete(){}

}