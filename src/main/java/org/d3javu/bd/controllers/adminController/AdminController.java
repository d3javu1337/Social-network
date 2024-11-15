package org.d3javu.bd.controllers.adminController;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.tag.TagDto;
import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.repositories.TagRepository;
import org.d3javu.bd.repositories.UserRepository;
import org.d3javu.bd.service.PostService;
import org.d3javu.bd.service.TagService;
import org.d3javu.bd.service.UserService;
import org.springframework.http.HttpStatus;
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
        model.addAttribute("tags", tagService.findAll());
        return "admin/tags";
    }

    @GetMapping("/tags/{id}")
    public String tag(Model model, @PathVariable("id") Long id ) {
        model.addAttribute("tag", tagService.findById(id).get());
        return "admin/tag";
    }

    @PostMapping("/tags/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute TagDto dto) {
        return this.tagService.update(id, dto)
                .map(t -> "redirect:/admin/tags/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));

    }

//        return userService.update(id, user)
//            .map(it -> "redirect:/users/{id}")
//            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
}
