//package org.d3javu.bd.auth;
//
//import lombok.RequiredArgsConstructor;
//import org.d3javu.bd.auth.userDetails.UserDetailsImpl;
//import org.d3javu.bd.auth.userDetails.UserDetailsMapper;
//import org.d3javu.bd.repositories.AuthDataRepository;
//import org.d3javu.bd.service.UserService;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@RequiredArgsConstructor
//@Component
//public class AuthProvider implements AuthenticationProvider {
//
//    private final UserDetailsService userDetailsService;
//    private final AuthDataRepository authDataRepository;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = authentication.getCredentials().toString();
//
////        UserDetails userDetails = userService.loadUserByUsername(username);
//        var authData = authDataRepository.findByLogin(username).orElse(null);
//        if(authData == null) {
//            throw new BadCredentialsException("Invalid username or password");
//        }
//
//        UserDetails userDetails = UserDetailsMapper.map(authData);
//
//        if(!userDetails.getPassword().equals(GetPasswordHash.hash(password))) {
//            throw new BadCredentialsException("Invalid password or username");
//        }
//        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
//
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//
//}
