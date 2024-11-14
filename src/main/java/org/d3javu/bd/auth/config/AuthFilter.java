//package org.d3javu.bd.auth.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Arrays;
//
//@RequiredArgsConstructor
//@Component
//public class AuthFilter extends OncePerRequestFilter {
//
//    private final UserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
////        String authHeader = request.getParameter("username");
////        String authHeader1 = request.getParameter("password");
//        System.out.println(request.getHeaderNames().toString());
////        System.out.println(Arrays.toString(request.getCookies()));
////        System.out.println(authHeader + " " + authHeader1);
//
////        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authHeader, authHeader1));
//        filterChain.doFilter(request, response);
//
////        if (authHeader != null && authHeader.startsWith("Bearer ")) {
////            authHeader = authHeader.substring(7);
////            String username = authHeader.substring(0, authHeader.indexOf(' '));
////            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
////        }
////        filterChain.doFilter(request, response);
//
//    }
//
//}
