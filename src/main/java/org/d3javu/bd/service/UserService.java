package org.d3javu.bd.service;

import lombok.RequiredArgsConstructor;
//import org.d3javu.bd.auth.authData.AuthData;
//import org.d3javu.bd.auth.userDetails.UserDetailsImpl;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

//import static org.d3javu.bd.models.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserEditMapper userEditMapper;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;
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
        var passwordHash = userCreateDto.getPassword();
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
        return this.userRepository.findById(id)
                .map(en -> this.userEditMapper.map(userEditDto, en))
                .map(this.userRepository::saveAndFlush)
                .map(this.userReadMapper::map);
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
        return this.userRepository.findByEmail(user).orElseThrow(() -> new UsernameNotFoundException(user));
    }
}
