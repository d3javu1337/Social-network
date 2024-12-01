package org.d3javu.bd.service;

import lombok.RequiredArgsConstructor;
//import org.d3javu.bd.auth.authData.AuthData;
//import org.d3javu.bd.auth.userDetails.UserDetailsImpl;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.dto.user.UserCreateDto;
import org.d3javu.bd.dto.user.UserEditDto;
import org.d3javu.bd.filter.user.UserFilter;
import org.d3javu.bd.dto.user.UserReadDto;
import org.d3javu.bd.mapper.user.UserCreateMapper;
import org.d3javu.bd.mapper.user.UserEditMapper;
import org.d3javu.bd.mapper.user.UserReadMapper;
import org.d3javu.bd.models.user.User;
//import org.d3javu.bd.repositories.AuthDataRepository;
import org.d3javu.bd.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//import static org.d3javu.bd.models.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserEditMapper userEditMapper;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final PostService postService;
//    private final AuthDataRepository authDataRepository;

//    public Page<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
//        var predicate = QPredicates.builder()
//                .add(filter.firstName(), user.firstName::containsIgnoreCase)
//                .add(filter.lastName(), user.lastName::containsIgnoreCase)
//                .add(filter.email(), user.email::containsIgnoreCase)
//                .buildAnd();
//        return userRepository.findAll(predicate, pageable).map(StaticUserReadMapper::map);
//    }

    public List<UserReadDto> findAll(UserFilter filter){
        return userRepository.findAllByFilter(filter)
                .stream().map(userReadMapper::map).toList();
    }

    public List<UserReadDto> findAll(){
        return userRepository.findAll().stream().map(userReadMapper::map).toList();
    }

    public Optional<UserReadDto> findById(Long id){
        return userRepository.findById(id).map(userReadMapper::map);
    }

    public Optional<UserReadDto> findByCustomLink(String customLink){
        return userRepository.findByCustomLink(customLink).map(userReadMapper::map);
    }

    @Transactional
    public UserReadDto create(UserEditDto userEditDto){
        return Optional.of(userEditDto)
                .map(userEditMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElse(null);
    }

    @Transactional
    public UserReadDto create(UserCreateDto userCreateDto){

        var login = userCreateDto.getLogin();
//        var passwordHash = GetPasswordHash.hash(userCreateDto.getPassword());
        var passwordHash = this.passwordEncoder.encode(userCreateDto.getPassword());
        userCreateDto.setPassword(passwordHash);
        org.d3javu.bd.models.user.User User = Optional.of(userCreateDto).map(userCreateMapper::map).get();
//        AuthData authData = new AuthData(login, passwordHash, User);
//                User.map(userRepository::save);
//        User.setAuthData(authData);
        User = userRepository.save(User);
//        authDataRepository.save(authData);

//        return User.map(userReadMapper::map).orElse(null);
        return userReadMapper.map(User);

//        return Optional.of(userCreateDto)
//                .map(userCreateMapper::map)
//                .map(userRepository::save)
//                .map(userReadMapper::map)
//                .orElse(null);
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserEditDto userEditDto){
        var user = this.userRepository.findByCustomLink(userEditDto.getCustomLink());
//        var user1 = this.userRepository.findById(userEditDto.id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(user.isPresent() && !id.equals(user.get().getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
             //need to do another but now ok
        }
//        System.out.println(userEditDto.customLink);
        if (userEditDto.getCustomLink() != null && !userEditDto.getCustomLink().isEmpty()) {
            if (userEditDto.getCustomLink().charAt(0) >= '0' && userEditDto.getCustomLink().charAt(0) <= '9') {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        return this.userRepository.findById(id)
                .map(en -> {
                    uploadAvatar(userEditDto.avatar);
                    return this.userEditMapper.map(userEditDto, en);
                })
                .map(this.userRepository::saveAndFlush)
                .map(this.userReadMapper::map);
    }

    @Transactional
    public void uploadAvatar(MultipartFile file) {
        if(!file.isEmpty()){
            try {
                imageService.uploadAvatar(file.getOriginalFilename(), file.getInputStream());
            }
            catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);//something other
            }
        }
    }

    @Transactional
    public boolean delete(Long id){
        return this.userRepository.findById(id)
                .map(en -> {
                    this.userRepository.delete(en);
                    this.userRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = authDataRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(username)).getUser();
//        return UserDetailsImpl.build(user);
        return userRepository.findByEmail(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User findByEmail(String user){
//        return this.userRepository.findByEmail(user).orElseThrow(() -> new UsernameNotFoundException(user));
//        return this.userRepository.findByEmail(user).orElse(null);
        return this.userRepository.findByEmail(user).get();
    }

    @Transactional
    public void follow(Long id, User user){
        this.userRepository.findById(id)
                .map(en -> {
                    user.follow(en);
                    this.userRepository.save(en);
                    this.userRepository.flush();
                    return true;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void follow(String customLink, User user){
        this.userRepository.findByCustomLink(customLink)
                .map(en -> {
                    user.follow(en);
                    this.userRepository.save(en);
                    this.userRepository.flush();
                    return true;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void unfollow(Long id, User user){
        this.userRepository.findById(id)
                .map(en -> {
                    user.unfollow(en);
                    this.userRepository.save(en);
                    this.userRepository.flush();
                    return true;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void unfollow(String customLink, User user){
        this.userRepository.findByCustomLink(customLink)
                .map(en -> {
                    user.unfollow(en);
                    this.userRepository.save(en);
                    this.userRepository.flush();
                    return true;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Optional<byte[]> findAvatar(Long id){
        return this.userRepository.findById(id)
                .map(User::getAvatarPath)
                .filter(StringUtils::hasText)
                .flatMap(imageService::getAvatar);
    }

    public Set<UserReadDto> findFollowsById(String id){
        long val;
        try{
            val = Long.parseLong(id);
        }catch (NumberFormatException e){
            val = this.findByCustomLink(id).orElseThrow(() -> new NotFoundException("user not found")).getId();
        }


        return this.userRepository.findFollowsById(val)
                .stream()
                .map(userReadMapper::map)
                .collect(Collectors.toSet());
    }

    public Set<PostReadDto> findPostsByUser(String id){
        long val;
        try {
            val = Long.parseLong(id);
        } catch (NumberFormatException e){
            val = this.findByCustomLink(id).orElseThrow(() -> new NotFoundException("user not found")).getId();
        }
        return this.postService.findAllByAuthorId(val);
    }

}
