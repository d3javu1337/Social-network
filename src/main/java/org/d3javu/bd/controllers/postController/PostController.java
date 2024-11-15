package org.d3javu.bd.controllers.postController;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.post.PostCreateDto;
import org.d3javu.bd.dto.post.PostEditDto;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.dto.tag.TagDto;
import org.d3javu.bd.dto.user.UserReadDto;
import org.d3javu.bd.mapper.post.PostEditMapper;
import org.d3javu.bd.mapper.post.PostReadMapper;
import org.d3javu.bd.mapper.tag.DtoToTagMapper;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.service.PostService;
import org.d3javu.bd.service.TagService;
import org.d3javu.bd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    private final PostEditMapper postEditMapper;
    private final PostReadMapper postReadMapper;
    private final DtoToTagMapper dtoToTagMapper;

    @GetMapping("/create")
    public String create(Model model) {
//        System.out.println(author+"-----------------------------------------------------------------");
        var dto = new PostCreateDto();
        var tags = this.tagService.findAll();
//        dto.author = author;
        model.addAttribute("post", dto);
        model.addAttribute("tags", tags);
        model.addAttribute("checked", new HashSet<>());
        return "/post/postCreate";
    }
    @PostMapping("/create")
    public String create(@ModelAttribute PostCreateDto postCreateDto, @ModelAttribute Post post) {
//        System.out.println(postCreateDto);
//        var ch = model.getAttribute("checked");
//        System.out.println(ch+"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        var user = SecurityContextHolder.getContext().getAuthentication().getName();
        var author = this.userService.findByEmail(user);
        postCreateDto.setAuthor(author);
        System.out.println(post+"++++++++++++++++++++++++++++++++");
//        postCreateDto.setTags(checked.stream().map(dtoToTagMapper::map).collect(Collectors.toSet()));
//        postCreateDto.setTags(tagDtos.stream().map(dtoToTagMapper::map).collect(Collectors.toSet()));
//        System.out.println(postCreateDto.author.getId()+"----------------------");
//        var userEmail = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
//        postCreateDto.author = this.userService.findByEmail(userEmail);
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
        var user = this.userService.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        var userId = user.getId();
//        System.out.println(user + "----------------------------------------------");
//        var user1 = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
//        var post = this.postService.findPostById(id).orElse(null);
//        post.view(user);
        postService.view(id, user);
//        System.out.println(user1);
//        var userId = user.getId();
//        System.out.println(user+"--------------------------------------");
//        model.addAttribute("user", user);
        return postService.findPostById(id)
                .map(p -> {
                    model.addAttribute("post", p);
                    model.addAttribute("userId", userId);
                    model.addAttribute("tags", this.tagService.findByPost(p));
                    model.addAttribute("authorId", p.getAuthor().getId());
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

    @PostMapping("/{id}/update/delete")
    public String delete(@PathVariable("id") Long id){
        var res = postService.delete(id);
        if(res){
            return "redirect:/posts";
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}