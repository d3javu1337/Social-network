package org.d3javu.bd.rest.v1;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.comment.CommentCreateDto;
import org.d3javu.bd.dto.comment.CommentEditDto;
import org.d3javu.bd.dto.comment.CommentReadDto;
import org.d3javu.bd.dto.user.CompactUserReadDto;
import org.d3javu.bd.service.CommentService;
import org.d3javu.bd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@RestController
public class CommentRestControllerV1 {

    private final CommentService commentService;
    private final UserService userService;


    @GetMapping(value = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getComments(@PathVariable long postId) {
        var currenUserId = this.getCurrentUserId();
        var comments = this.commentService.findAllByPostId(postId, currenUserId);
        var map = new HashMap<String, Object>();
        map.put("comments", comments);
        map.put("currentUser", this.getCurrentUser());
        return map;
    }


    @GetMapping("/{commentId}/like")
    public ResponseEntity<CommentReadDto> like(@PathVariable long commentId) {
        this.commentService.like(commentId, this.getCurrentUserId());
        return new ResponseEntity<>(this.commentService.findById(commentId, this.getCurrentUserId()), HttpStatus.OK);
    }

    @GetMapping("/{commentId}/unlike")
    public ResponseEntity<CommentReadDto> unlike(@PathVariable long commentId) {
        this.commentService.unlike(commentId, this.getCurrentUserId());
        return new ResponseEntity<>(this.commentService.findById(commentId, this.getCurrentUserId()), HttpStatus.OK);
    }

    @PostMapping("/{commentId}/edit")
    public ResponseEntity<CommentReadDto> edit(@PathVariable long commentId, @RequestBody CommentEditDto comment) {
        var comm = this.commentService.update(commentId, this.getCurrentUserId(), comment);
        return new ResponseEntity<>(comm, HttpStatus.OK);
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<?> create(@RequestParam String body, @PathVariable long postId) {
        if(body.isBlank()) {
            return new ResponseEntity<>("Comment body can not be blank", HttpStatus.BAD_REQUEST);
        }
        var commentCreateDto = new CommentCreateDto();
        commentCreateDto.setBody(body);
        var userId = this.getCurrentUserId();
        var comment = this.commentService.create(postId, userId, commentCreateDto);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }


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