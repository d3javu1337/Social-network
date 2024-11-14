//package org.d3javu.bd.auth.userDetails;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import org.d3javu.bd.dto.user.UserCreateDto;
//import org.d3javu.bd.models.user.User;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//public class UserDetailsImpl implements UserDetails {
//
//    private Long id;
//    private String login;
//    private String password;
//    private Collection<? extends GrantedAuthority> authorities;
//
//    public static UserDetailsImpl build(User user) {
//        return new UserDetailsImpl(
//                user.getId(),
//                user.getAuthData().getLogin(),
//                user.getAuthData().getPasswordHash(),
//                List.of(user.getAuthData().getRole())
//        );
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Override
//    public String getUsername() {
//        return this.login;
//    }
//
////    public String getLogin() {
////        return "";
////    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
//    }
//}
