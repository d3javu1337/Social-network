//package org.d3javu.bd.controllers.commentController;
//
//import lombok.RequiredArgsConstructor;
//import org.d3javu.bd.service.CommentService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@RequestMapping("/posts/{id}/comments")
//@RequiredArgsConstructor
//@Controller
//public class CommentController {
//
//    private final CommentService commentService;
//
//    @GetMapping
//    public String findAll(@PathVariable("id") Long id, Model model) {
//        var comments = this.commentService.findAllByPostId(id);
//        model.addAttribute("comments", comments);
//        return "comment/comments";
//    }
//
//}
