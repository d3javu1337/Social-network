package org.d3javu.bd.controllers.authController;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.user.UserCreateDto;
import org.d3javu.bd.dto.user.UserEditDto;
import org.d3javu.bd.dto.user.UserReadDto;
import org.d3javu.bd.service.TagService;
import org.d3javu.bd.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;

@RequestMapping
@RequiredArgsConstructor
@Controller
public class AuthController {

    private final UserService userService;
    private final TagService tagService;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }



    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("user", new UserCreateDto());
        model.addAttribute("tags", tagService.findAll());
        return "user/registration";
    }

    @PostMapping("/registration")
    public String create(@ModelAttribute UserCreateDto user, RedirectAttributes redirectAttributes){
//        System.out.println(user);
        if (false){
//            redirectAttributes.addAttribute("")
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/users/registration";
        }
        UserReadDto dto = userService.create(user);
//        return "redirect:/users/" + dto.getId();
        return "redirect:/login";
    }



//    @GetMapping("/done")
//    public String authDone(Model model){
//        return "redirect:localhost:8080/index";
//    }

}
