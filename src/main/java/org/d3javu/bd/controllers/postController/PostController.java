package org.d3javu.bd.controllers.postController;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.post.PostCreateDto;
import org.d3javu.bd.dto.post.PostEditDto;
import org.d3javu.bd.dto.tag.PreferredTagsDto;
import org.d3javu.bd.dto.user.CompactUserReadDto;
import org.d3javu.bd.mapper.post.PostEditMapper;
import org.d3javu.bd.mapper.post.PostReadMapper;
import org.d3javu.bd.mapper.tag.DtoToTagMapper;
import org.d3javu.bd.mapper.user.UserReadMapper;
import org.d3javu.bd.models.post.PostForReport;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
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
        return "post/postCreate";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute PostCreateDto postCreateDto, @ModelAttribute PreferredTagsDto chosenTags) {
        var user = SecurityContextHolder.getContext().getAuthentication().getName();
        var author = this.userService.findByEmail(user);
        postCreateDto.setAuthor(author);
        postCreateDto.setTags(chosenTags.getTags());
//        System.out.println(post+"++++++++++++++++++++++++++++++++");
        var p = this.postService.create(postCreateDto);
        return "redirect:/posts/" + p.getId();
    }

    @GetMapping("/preferred")
    public String findAllPreferred(Model model) {

        var tags = this.userService.findPreferredTagsByUserId(this.getCurrentUserId());

        var posts = this.postService.findByPreferred(tags, this.getCurrentUserId());

        System.out.println("---------------------------------------------------");
        System.out.println(posts);
        System.out.println("---------------------------------------------------");
//                .stream()
//                .sorted(Comparator.comparing(en -> en.createdAt))
//                .collect(Collectors.toList());
//        List<PostReadDto> posts = this.postService.findByPreferred(tags);
        model.addAttribute("posts", posts);
        model.addAttribute("currentUser", this.getCurrentUser());
        model.addAttribute("tags", this.tagService.findAll());

//        System.out.println("----------------------");
//        System.out.println(model.asMap().keySet());
//        System.out.println("----------------------");

        return "post/posts";
    }

    @GetMapping
    public String findAll(Model model) {
        var posts = this.postService.findAll(this.getCurrentUserId());
//        var posts = this.postService.findAll(31L);
//        System.out.println("----------------------------------------");
//        System.out.println(posts.size());
//        System.out.println("----------------------------------------");
        model.addAttribute("posts", posts);
        model.addAttribute("currentUser", this.getCurrentUser());
//        model.addAttribute("currentUser", new CompactUserReadDto(31L, "", "", ""));
        model.addAttribute("tags", this.tagService.findAll());
        return "post/posts";
    }

    @GetMapping("/byids")
    public String findAllByIds(Model model, @RequestAttribute List<Long> ids) {
//        List<Long> ids = new ArrayList<>();
//        for(var x : vals.keySet()) {
//            ids = vals.get(x);
//        }
//        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("------------------------");
//        System.out.println(posts);
//        System.out.println(ids);
//        System.out.println("------------------------");
        var posts = this.postService.findAllByIds(Set.of(ids.toArray(new Long[0])));
        model.addAttribute("posts", posts);
//        model.addAttribute("currentUser", this.getCurrentUser());
        return "post/report";
    }

    @GetMapping("/bytags")
    public String findByTags(Model model, @ModelAttribute PreferredTagsDto tags) {
        var posts = this.postService.findByTags(tags.getTags());
        model.addAttribute("posts", posts);
        model.addAttribute("currentUser", this.userService.findById(this.getCurrentUserId()));
        model.addAttribute("tags", this.tagService.findAll());
        return "post/posts";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        var user = this.userService.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        var userId = user.getId();
        postService.view(id, user);
        return postService.findPostById(id, userId)
                .map(p -> {
                    model.addAttribute("post", p);
//                    model.addAttribute("userId", userId);
//                    model.addAttribute("tags", this.tagService.findByPost(p));
//                    model.addAttribute("authorId", p.getAuthor().getId());
//                    model.addAttribute("comments", commentService.findAllByPostId(id));
//                    model.addAttribute("isLiked", p.getLikes().contains(user));
                    model.addAttribute("currentUser", user);
                    return "post/post";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/tag/{id}")
    public String findAllByTag(@PathVariable("id") Long tagId, Model model) {
        model.addAttribute("posts", postService.findAllByTagId(tagId));
        model.addAttribute("currentUser", this.userService.findById(this.getCurrentUserId()));
        return "post/posts";
    }

    @GetMapping("/{id}/update")
    public String update(Model model, @PathVariable Long id) {
        var post = this.postService.findById(id).get();
        model.addAttribute("post", post);
        model.addAttribute("tags", this.tagService.findAll());
        model.addAttribute("currentUser", this.userService.findById(this.getCurrentUserId()));
        return "post/postUpdate";
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
        this.postService.like(id, user.getId());
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/unlike")
    public String unlike(@PathVariable("id") Long id) {
        var user = this.userService.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        this.postService.unlike(id, user.getId());
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/images/{path}/delete")
    public String deleteImage(@PathVariable("id") Long id, @PathVariable("path") String path) {
        this.imageService.deleteImage(id, path);
        return "redirect:/posts/%s/update".formatted(id);
    }

//    @Deprecated(forRemoval = true)
//    public User getCurrentUser(){
//        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        return this.userService.findByEmail(userEmail);
//    }

    public CompactUserReadDto getCurrentUser(){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findById(this.userService.findIdByEmail(userEmail))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long getCurrentUserId(){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findIdByEmail(userEmail);
    }

}

//    @GetMapping("/{id}/liked")


