package org.d3javu.bd.controllers.userController;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.tag.PreferredTagsDto;
import org.d3javu.bd.dto.user.CompactUserEditDto;
import org.d3javu.bd.dto.user.CompactUserReadDto;
import org.d3javu.bd.dto.user.UserEditDto;
import org.d3javu.bd.filter.user.UserFilter;
import org.d3javu.bd.mapper.user.CompactUserReadMapper;
import org.d3javu.bd.mapper.user.UserReadMapper;
import org.d3javu.bd.repositories.UserRepository;
import org.d3javu.bd.service.CommentService;
import org.d3javu.bd.service.PostService;
import org.d3javu.bd.service.TagService;
import org.d3javu.bd.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@RequestMapping("/users")
@RequiredArgsConstructor
@org.springframework.stereotype.Controller
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final UserReadMapper userReadMapper;
    private final TagService tagService;
    private final CompactUserReadMapper compactUserReadMapper;

    @GetMapping
    public String findAll(Model model, UserFilter filter, Pageable pageable) {
//        model.addAttribute("users", userService.findAll(filter, pageable));
        model.addAttribute("currentUser", this.getCurrentUser());
        model.addAttribute("users", userService.findAll(filter));
        return "user/users";
    }

    //    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") String id, Model model){
        model.addAttribute("follows", this.userService.findFollowsById(id));
        model.addAttribute("posts", this.userService.findPostsByUser(id));
        model.addAttribute("chosenTags", new PreferredTagsDto(new HashSet<>()));
        model.addAttribute("tags", this.tagService.findAll());
        if(isNumber(id)){
            var val = Long.parseLong(id);
            model.addAttribute("followsCount", this.userService.getFollowsCountById(val));
            model.addAttribute("followersCount", this.userService.getFollowersCountById(val));
            return userService.findById(val)
                    .map(user -> {
                        model.addAttribute("user", user);
//                        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
                        var currentUser = this.getCurrentUser();
                        model.addAttribute("currentUser", currentUser);

//                        var bool = this.userService.findByEmail(user.getUsername()).getFollowers().contains(currentUser);
                        var bool = this.userService.isFollowed(user.getId(), currentUser.getId());
                        model.addAttribute("isFollowed", bool);
//                        if(currentUser.getId() == val) return "user/user";
//                        else return "user/userProfile";
                        return "user/userProfile";
                    })
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }else{
            return userService.findByCustomLink(id)
                    .map(user -> {
                        model.addAttribute("user", user);
                        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
                        var currentUser = this.userService.findByEmail(userEmail);
                        model.addAttribute("followsCount", this.userService.getFollowsCountById(currentUser.getId()));
                        model.addAttribute("followersCount", this.userService.getFollowersCountById(currentUser.getId()));
                        var bool = this.userService.findByEmail(user.getUsername()).getFollowers().contains(currentUser);
                        model.addAttribute("currentUser", this.compactUserReadMapper.map(currentUser));
                        model.addAttribute("isFollowed", bool);
//                        if(currentUser.getCustomLink().equals(id)) return "user/user";
//                        else return "user/userProfile";
                        return "user/userProfile";
                    })
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
    }

//    @PostMapping("/registration")
//    public String create(@ModelAttribute UserEditDto user, RedirectAttributes redirectAttributes){
//        if (false){
////            redirectAttributes.addAttribute("")
//            redirectAttributes.addFlashAttribute("user", user);
//            return "redirect:/users/registration";
//        }
//        UserReadDto dto = userService.create(user);
//        return "redirect:/users/" + dto.getId();
//    }

    @GetMapping("/{id}/update")
    public String forUpdate(@PathVariable("id") Long id, Model model){
        var currentUserId = this.getCurrentUserId();
        model.addAttribute("user", this.userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        if (currentUserId.equals(id)) return "user/user";
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);

//        if(!Objects.equals(id, currentUser.getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
//        return userService.update(id, user)
//                .map(it -> "redirect:/users/{id}")
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        return "redirect:/users/{id}";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute CompactUserEditDto user){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = this.userService.findByEmail(userEmail);
//        System.out.printf("%s %s %s %s %s %s %s \n", id, currentUser.getId(), Objects.equals(id, currentUser.getId()),
//                user.getCustomLink(), user.getCustomLink() == null, user.customLink.isBlank(), user.customLink.isEmpty());
        if(!Objects.equals(id, currentUser.getId())) {throw new ResponseStatusException(HttpStatus.FORBIDDEN);}
        return userService.update(id, user)
                .map(it -> "redirect:/users/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        return "redirect:/users/{id}";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id){

        return "redirect:/users/";
    }

    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute("user") UserEditDto user){
        model.addAttribute("user", user);
        return "user/registration";
    }

    @PostMapping("/{id}/follow")
    public String follow(@PathVariable("id") String id){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = this.userService.findByEmail(userEmail);
        if(isNumber(id)){
            var val = Long.parseLong(id);
            this.userService.follow(val, currentUser);
        }else{
            this.userService.follow(id, currentUser);
        }
        return "redirect:/users/" + id;
    }

    @PostMapping("/{id}/unfollow")
    public String unfollow(@PathVariable("id") String id){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = this.userService.findByEmail(userEmail);
        if(isNumber(id)){
            var val = Long.parseLong(id);
            this.userService.unfollow(val, currentUser);
        }else{
            this.userService.unfollow(id, currentUser);
        }
        return "redirect:/users/" + id;
    }

    @GetMapping("/follows/{id}")
    public String followers(@PathVariable("id") String id, Model model){
        var users = this.userService.findFollowsById(id)
                .stream()
                .map(en -> new CompactUserReadDto(
                        en.getId(),
                        en.getFirstName(),
                        en.getLastName(),
                        en.getAvatar()
                ))
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        model.addAttribute("currentUser", this.getCurrentUser());
        return "user/users";
    }

    @GetMapping("/followers/{id}")
    public String follows(@PathVariable("id") String id, Model model){
        model.addAttribute("currentUser", this.getCurrentUser());
        Long val;
        try{
            val = Long.parseLong(id);
        }catch(NumberFormatException e){
            val = this.userService.findByCustomLink(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getId();
        }
        model.addAttribute("users", this.userService.findFollowersById(id));
        return "user/users";
    }

//    @GetMapping("/liked/post/{id}")
//    public String likedPost(@PathVariable("id") Long id, Model model){
//        var liked = this.postService.findById(id)
//                .map(PostReadDto::getLikes)
////                .map(e -> (Set<CommentReadDto>)e. )
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        model.addAttribute("users", liked);
//        return "user/users";
//    }


//    @GetMapping("/liked/comment/{id}")
//    public String likedComment(@PathVariable("id") Long id, Model model){
//        var liked = this.commentService.findById(id)
////                .map(CommentReadDto::getLikes)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
//                .getLikes();
////                .stream()
////                .map(this.userReadMapper::map);
////                .collect(Collectors.toSet());
//        model.addAttribute("users", liked);
//        return "user/users";
//    }


    public boolean isNumber(String id){
        try{
            var val = Long.parseLong(id);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
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
