package org.d3javu.bd.rest;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.user.UserEditDto;
import org.d3javu.bd.dto.user.UserReadDto;
import org.d3javu.bd.repositories.UserRepository;
import org.d3javu.bd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserRestController {

    private final UserRepository userRepository;
    private final UserService userService;

//    @GetMapping
//    @ResponseBody
//    public PageResponse<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
//        userService.findAll(filter, pageable);
//        Page<UserReadDto> page = userService.findAll(filter, pageable);
//        return PageResponse.of(page);
//    }

    @GetMapping("/{id}")
    public UserReadDto findById(@PathVariable("id") Long id){
//        model.addAttribute("user", userRepository.findById(id));
//        return "users";
        return userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserReadDto create(@RequestBody UserEditDto user){
        return userService.create(user);
//        return "redirect:/users/" + dto.getId();
    }

    @PutMapping("/{id}")
    public UserReadDto update(@PathVariable("id") Long id, @RequestBody UserEditDto user){
        return userService.update(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        return "redirect:/users/{id}";
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        if(!userService.delete(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{id}/avatar", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] findAvatar(@PathVariable("id") Long id){
        return this.userService.findAvatar(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

//    @GetMapping("/registration")
//    public String registration(Model model, @ModelAttribute("user") UserCreateEditDto user){
//        model.addAttribute("user", user);
//        return "user/registration";
//    }


}
