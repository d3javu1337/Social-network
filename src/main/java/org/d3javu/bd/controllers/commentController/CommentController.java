package org.d3javu.bd.controllers.commentController;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.comment.CommentCreateDto;
import org.d3javu.bd.dto.comment.CommentEditDto;
import org.d3javu.bd.dto.comment.CommentReadDto;
import org.d3javu.bd.mapper.comment.CommentReadMapper;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.service.CommentService;
import org.d3javu.bd.service.PostService;
import org.d3javu.bd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

@RequestMapping("/posts/{postId}/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final CommentReadMapper commentReadMapper;
//    private final PostService postService;


    @GetMapping("/all")
    public String all(@PathVariable Long postId, Model model) {
        model.addAttribute("comments", this.commentService.findAllByPostId(postId));
        return "comment/comments";
    }

    @PostMapping("/{id}/like")
    public String like(@PathVariable("id") Long commentId, @PathVariable("postId") Long postId) {

        var user = this.userService.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        this.commentService.like(commentId, user);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{id}/unlike")
    public String unlike(@PathVariable("id") Long commentId, @PathVariable("postId") Long postId) {

        var user = this.userService.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        this.commentService.unlike(commentId, user);

        return "redirect:/posts/"+postId;
    }

    @PostMapping("/new")
    public String createComment(@PathVariable("postId") Long postId, @ModelAttribute CommentCreateDto commentCreateDto) {
//        var user = this.userService.findByEmail(SecurityContextHolder.getContext()
//                .getAuthentication().getName());
        commentCreateDto.user = this.getCurrentUser();
        this.commentService.create(postId, commentCreateDto);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long commentId,@PathVariable("postId") Long postId, Model model) {
//        var user = this.userService.findByEmail(SecurityContextHolder.getContext()
//                .getAuthentication().getName());
        var user = this.getCurrentUser();
        var comment = this.commentService.findById(commentId)
                .orElseThrow(() -> new NotFoundException("not found id : "+commentId));
        if(!user.getId().equals(comment.getAuthor().getId())) {throw new ResponseStatusException(HttpStatus.FORBIDDEN);}
        model.addAttribute("comment", comment);
        model.addAttribute("postId", postId);
        model.addAttribute("currentUser",user);
        return "comment/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@ModelAttribute CommentEditDto comment, @PathVariable("postId") Long postId,
                         @PathVariable("id") Long commentId, Model model) {
        this.commentService.update(commentId, comment);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{id}/edit/delete")
    public String delete(@PathVariable("postId") Long postId, @PathVariable("id") Long commentId) {
        var bool = this.commentService.delete(commentId);
        var comm = this.commentService.findById(commentId);
        System.out.println(bool + " " + comm);
        return "redirect:/posts/" + postId;
    }
//
//    @GetMapping("/all")
//    public String all(@PathVariable("postId") Long postId, Model model) {
//
//
//        return "comment/comments";
//    }

    public User getCurrentUser(){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findByEmail(userEmail);
    }


}
