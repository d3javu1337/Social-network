//package org.d3javu.bd.auth.userDetails;
//
//import org.d3javu.bd.auth.authData.AuthData;
//import org.d3javu.bd.mapper.Mapper;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class UserDetailsMapper {
////    @Override
//    public static UserDetailsImpl map(AuthData object) {
//        UserDetailsImpl userDetails = new UserDetailsImpl(
//                object.getUser().getId(),
//                object.getLogin(),
//                object.getPasswordHash(),
//                List.of(object.getRole())
//        );
//        return userDetails;
//    }
//
////    @Override
//    public static UserDetailsImpl map(AuthData from, UserDetailsImpl to) {
//        to.setId(from.getUser().getId());
//        to.setLogin(from.getLogin());
//        to.setPassword(from.getPasswordHash());
//        to.setAuthorities(List.of(from.getRole()));
//        return to;
//    }
//}
