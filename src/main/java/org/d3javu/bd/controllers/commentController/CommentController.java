package org.d3javu.bd.controllers.commentController;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.comment.CommentCreateDto;
import org.d3javu.bd.dto.comment.CommentEditDto;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.service.CommentService;
import org.d3javu.bd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/posts/{postId}/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;


    @GetMapping("/all")
    public String all(@PathVariable Long postId, Model model) {
        var currentUserId = this.getCurrentUserId();
        model.addAttribute("comments", this.commentService.findAllByPostId(postId, currentUserId));
        return "comment/comments";
    }

    @PostMapping("/{id}/like")
    public String like(@PathVariable("id") Long commentId, @PathVariable("postId") Long postId) {

        this.commentService.like(commentId, this.getCurrentUserId());

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{id}/unlike")
    public String unlike(@PathVariable("id") Long commentId, @PathVariable("postId") Long postId) {

        this.commentService.unlike(commentId, this.getCurrentUserId());

        return "redirect:/posts/"+postId;
    }

    @PostMapping("/new")
    public String createComment(@PathVariable("postId") Long postId, @ModelAttribute CommentCreateDto commentCreateDto) {
        commentCreateDto.userId = this.getCurrentUser().getId();
        this.commentService.create(postId, this.getCurrentUserId(), commentCreateDto);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long commentId,@PathVariable("postId") Long postId, Model model) {
        var user = this.getCurrentUser();
        var comment = this.commentService.findById(commentId, this.getCurrentUserId());
        if(!user.getId().equals(comment.author.getId())) {throw new ResponseStatusException(HttpStatus.FORBIDDEN);}
        model.addAttribute("comment", comment);
        model.addAttribute("postId", postId);
        model.addAttribute("currentUser",user);
        return "comment/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@ModelAttribute CommentEditDto comment, @PathVariable("postId") Long postId,
                         @PathVariable("id") Long commentId, Model model) {
        this.commentService.update(commentId, this.getCurrentUserId() ,comment);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{id}/edit/delete")
    public String delete(@PathVariable("postId") Long postId, @PathVariable("id") Long commentId) {
        this.commentService.delete(commentId);
        return "redirect:/posts/" + postId;
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


}
