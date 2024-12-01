package org.d3javu.bd.controllers.postController;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.post.PostCreateDto;
import org.d3javu.bd.dto.post.PostEditDto;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.mapper.post.PostCreateMapper;
import org.d3javu.bd.mapper.post.PostEditMapper;
import org.d3javu.bd.mapper.post.PostReadMapper;
import org.d3javu.bd.mapper.tag.DtoToTagMapper;
import org.d3javu.bd.mapper.user.UserReadMapper;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/posts")
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final TagService tagService;
    private final CommentService commentService;
    private final ImageService imageService;
    private final PostEditMapper postEditMapper;
    private final PostReadMapper postReadMapper;
    private final DtoToTagMapper dtoToTagMapper;
    private final UserReadMapper userReadMapper;

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
    public String create(@ModelAttribute PostCreateDto postCreateDto) {
        var user = SecurityContextHolder.getContext().getAuthentication().getName();
        var author = this.userService.findByEmail(user);
        postCreateDto.setAuthor(author);
//        System.out.println(post+"++++++++++++++++++++++++++++++++");
        var p = this.postService.create(postCreateDto);
        return "redirect:/posts/" + p.getId();
    }

    @GetMapping("/preferred")
    public String findAllPreferred(Model model) {
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = this.userService.findByEmail(userEmail);
        var tags = user.getPreferredTags();
        var posts = this.postService.findAll()
                .stream()
                .sorted(Comparator.comparing(en -> en.createdAt))
                .collect(Collectors.toList());
        var currentUser = this.userReadMapper.map(this.userService.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName()));
//        List<PostReadDto> posts = this.postService.findByPreferred(tags);
        model.addAttribute("posts", posts);
        model.addAttribute("currentUser", currentUser);
        return "/post/posts";
    }

    @GetMapping
    public String findAll(Model model) {
        var user = this.userReadMapper.map(this.userService.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName()));
        var posts = this.postService.findAll()
                .stream()
                .sorted(Comparator.comparing(en -> en.createdAt))
                .collect(Collectors.toList());
        model.addAttribute("posts", posts);
        model.addAttribute("currentUser", user);
        return "/post/posts";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
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
                    model.addAttribute("currentUser", user);
                    return "/post/post";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/tag/{id}")
    public String findAllByTag(@PathVariable("id") Long tagId, Model model) {
        model.addAttribute("posts", postService.findAllByTagId(tagId));
        model.addAttribute("currentUser", this.getCurrentUser());
        return "/post/posts";
    }

    @GetMapping("/{id}/update")
    public String update(Model model, @PathVariable Long id) {
        var post = this.postService.findById(id).get();
        model.addAttribute("post", post);
        model.addAttribute("tags", this.tagService.findAll());
        return "/post/postUpdate";
    }


    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute PostEditDto postEditDto) {
        return postService.update(id, postEditDto)
                .map(it -> "redirect:/posts/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update/delete")
    public String delete(@PathVariable("id") Long id) {
        var res = postService.delete(id);
        if (res) {
            return "redirect:/posts";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/like")
    public String like(@PathVariable("id") Long id) {
        var user = this.userService.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        this.postService.like(id, user);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/unlike")
    public String unlike(@PathVariable("id") Long id) {
        var user = this.userService.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        this.postService.unlike(id, user);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/images/{path}/delete")
    public String deleteImage(@PathVariable("id") Long id, @PathVariable("path") String path) {
        this.imageService.deleteImage(id, path);
        return "redirect:/posts/%s/update".formatted(id);
    }

    public User getCurrentUser(){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findByEmail(userEmail);
    }

}

//    @GetMapping("/{id}/liked")


