package org.d3javu.bd.controllers.postController;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.post.PostCreateDto;
import org.d3javu.bd.dto.post.PostEditDto;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.mapper.post.PostEditMapper;
import org.d3javu.bd.mapper.post.PostReadMapper;
import org.d3javu.bd.mapper.tag.DtoToTagMapper;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.service.CommentService;
import org.d3javu.bd.service.PostService;
import org.d3javu.bd.service.TagService;
import org.d3javu.bd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;

@RequestMapping("/posts")
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final TagService tagService;
    private final CommentService commentService;
    private final PostEditMapper postEditMapper;
    private final PostReadMapper postReadMapper;
    private final DtoToTagMapper dtoToTagMapper;

    @GetMapping("/create")
    public String create(Model model) {
        var dto = new PostCreateDto();
        var tags = this.tagService.findAll();
        model.addAttribute("post", dto);
        model.addAttribute("tags", tags);
        model.addAttribute("checked", new HashSet<>());
        return "/post/postCreate";
    }
    @PostMapping("/create")
    public String create(@ModelAttribute PostCreateDto postCreateDto, @ModelAttribute Post post) {
        var user = SecurityContextHolder.getContext().getAuthentication().getName();
        var author = this.userService.findByEmail(user);
        postCreateDto.setAuthor(author);
        System.out.println(post+"++++++++++++++++++++++++++++++++");
        var p = this.postService.create(postCreateDto);
        return "redirect:/posts/" + p.getId();
    }

    @GetMapping("/preferred")
    public String findAllPreferred(Model model) {
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = this.userService.findByEmail(userEmail);
        var tags = user.getPreferredTags();
        List<PostReadDto> posts = this.postService.findByPreferred(tags);
        model.addAttribute("posts", posts);
        return "/post/posts";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("posts", this.postService.findAll());
        return "/post/posts";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model){
        var user = this.userService.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        var userId = user.getId();
        postService.view(id, user);
        return postService.findPostById(id)
                .map(p -> {
                    model.addAttribute("post", p);
                    model.addAttribute("userId", userId);
                    model.addAttribute("tags", this.tagService.findByPost(p));
                    model.addAttribute("authorId", p.getAuthor().getId());
                    model.addAttribute("comments", commentService.findAllByPostId(id));
                    model.addAttribute("isLiked", p.getLikes().contains(user));
                    return "/post/post";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/tag/{id}")
    public String findAllByTag(@PathVariable("id") Long tagId, Model model) {
        model.addAttribute("posts", postService.findAllByTagId(tagId));
        return "/post/posts";
    }

    @GetMapping("/{id}/update")
    public String update(Model model, @PathVariable Long id){
        var post = this.postService.findById(id).get();
        model.addAttribute("post", post);
        model.addAttribute("tags", this.tagService.findAll());
        return "/post/postUpdate";
    }


    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute PostEditDto postEditDto){
        return postService.update(id, postEditDto)
                .map(it -> "redirect:/posts/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update/delete")
    public String delete(@PathVariable("id") Long id){
        var res = postService.delete(id);
        if(res){
            return "redirect:/posts";
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/like")
    public String like(@PathVariable("id") Long id){
        var user = this.userService.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        this.postService.like(id, user);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/unlike")
    public String unlike(@PathVariable("id") Long id){
        var user = this.userService.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        this.postService.unlike(id, user);
        return "redirect:/posts/" + id;
    }

}