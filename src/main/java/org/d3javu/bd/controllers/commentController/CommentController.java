package org.d3javu.bd.controllers.commentController;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.service.CommentService;
import org.d3javu.bd.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/posts/{postId}/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

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

}
