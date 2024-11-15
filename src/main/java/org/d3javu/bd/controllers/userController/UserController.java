package org.d3javu.bd.controllers.userController;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.user.UserEditDto;
import org.d3javu.bd.dto.user.UserFilter;
import org.d3javu.bd.dto.user.UserReadDto;
import org.d3javu.bd.repositories.UserRepository;
import org.d3javu.bd.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/users")
@RequiredArgsConstructor
@org.springframework.stereotype.Controller
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping
    public String findAll(Model model, UserFilter filter, Pageable pageable) {
//        model.addAttribute("users", userService.findAll(filter, pageable));
        model.addAttribute("users", userService.findAll(filter));
        return "user/users";
    }

    //    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model){
//        model.addAttribute("user", userRepository.findById(id));
//        return "users";
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
                    var currentUser = this.userService.findByEmail(userEmail);
                    if(currentUser.getId() == id) return "user/user";
                    else return "user/userProfile";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/registration")
    public String create(@ModelAttribute UserEditDto user, RedirectAttributes redirectAttributes){
        if (false){
//            redirectAttributes.addAttribute("")
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/users/registration";
        }
        UserReadDto dto = userService.create(user);
        return "redirect:/users/" + dto.getId();
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute UserEditDto user){
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
}
