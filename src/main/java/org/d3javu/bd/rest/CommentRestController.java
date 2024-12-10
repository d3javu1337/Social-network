package org.d3javu.bd.rest;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.comment.CommentCreateDto;
import org.d3javu.bd.dto.comment.CommentReadDto;
import org.d3javu.bd.mapper.comment.CommentReadMapper;
import org.d3javu.bd.mapper.user.CompactUserReadMapper;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.service.CommentService;
import org.d3javu.bd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@RestController
public class CommentRestController {

    private final CommentService commentService;
    private final CommentReadMapper commentReadMapper;
    private final UserService userService;
    private final CompactUserReadMapper compactUserReadMapper;


    @GetMapping(value = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<CommentReadDto> getComments(@PathVariable long postId) {
    public Map<String, Object> getComments(@PathVariable long postId) {
//        System.out.println(123);
        var comments = new ArrayList<>(this.commentService.findAllByPostId(postId));
//        System.out.println(comments.get(0).post);
        var currentUser = this.getCurrentUser();
        var map = new HashMap<String, Object>();
        map.put("comments", comments);
        map.put("currentUser", this.compactUserReadMapper.map(currentUser));
        return map;
    }

    @GetMapping("/{commentId}/like")
    public ResponseEntity<CommentReadDto> like(@PathVariable long commentId) {
        this.commentService.like(commentId,this.getCurrentUser());
        var comment = this.commentService.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @GetMapping("/{commentId}/unlike")
    public ResponseEntity<CommentReadDto> unlike(@PathVariable long commentId) {
        this.commentService.unlike(commentId,this.getCurrentUser());
        var comment = this.commentService.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/edit")
    public ResponseEntity<CommentReadDto> edit(@PathVariable long commentId, @RequestBody CommentReadDto comment) {

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<CommentReadDto> create(@RequestParam String body, @PathVariable long postId) {
        var commentCreateDto = new CommentCreateDto();
        commentCreateDto.setBody(body);
        commentCreateDto.setUser(this.getCurrentUser());
        var comment = this.commentService.create(postId, commentCreateDto);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }


    public User getCurrentUser(){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findByEmail(userEmail);
    }

}