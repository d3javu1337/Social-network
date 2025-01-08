package org.d3javu.bd.controllers.adminController;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.tag.TagDto;
import org.d3javu.bd.dto.user.CompactUserReadDto;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.service.PostService;
import org.d3javu.bd.service.TagService;
import org.d3javu.bd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final TagService tagService;
    private final UserService userService;
//    private final CommentService commentService;
    private final PostService postService;

    @GetMapping("/tags")
    public String tags(Model model) {
        model.addAttribute("currentUser", this.getCurrentUser());
        model.addAttribute("tags", tagService.findAll());
        return "admin/admin";
    }

    @GetMapping("/tags/{id}")
    public String tag(Model model, @PathVariable("id") Long id ) {
        model.addAttribute("tag", tagService.findById(id).orElseThrow
                (() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        model.addAttribute("currentUser", this.getCurrentUser());
        return "admin/tag";
    }

    @PostMapping("/tags/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute TagDto dto) {
        return this.tagService.update(id, dto)
                .map(t -> "redirect:/admin/tags")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));

    }

    @PostMapping("/tags/new")
    public String add(@ModelAttribute TagDto dto) {
        if(this.tagService.findByBody(dto.getBody()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate tag");
        else{
            this.tagService.create(dto);
            return "redirect:/admin/admin";
        }
    }
//
//    @PostMapping("/posts/best/{count}")
//    public String posts(@PathVariable("count") Integer count) {
//        return "redirect:/admin/posts/report";
//    }
//
//    @GetMapping("/posts/report")
//    public String report(Model model) {
//        return "";
//    }

    public CompactUserReadDto getCurrentUser(){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findById(this.userService.findIdByEmail(userEmail))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

//        return userService.update(id, user)
//            .map(it -> "redirect:/users/{id}")
//            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
}
