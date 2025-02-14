package org.d3javu.bd.rest.v1;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.user.CompactUserEditDto;
import org.d3javu.bd.dto.user.UserEditDto;
import org.d3javu.bd.dto.user.UserReadDto;
import org.d3javu.bd.models.user.IUser;
import org.d3javu.bd.repositories.UserRepository;
import org.d3javu.bd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserRestControllerV1 {

    private final UserRepository userRepository;
    private final UserService userService;


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IUser findById(@PathVariable("id") Long id){
        return userService.findIUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserReadDto create(@RequestBody UserEditDto user){
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public UserReadDto update(@PathVariable("id") Long id, @RequestBody CompactUserEditDto user){
        return userService.update(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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

}
