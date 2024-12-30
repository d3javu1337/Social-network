package org.d3javu.bd.auth.config;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.webjars.NotFoundException;

import java.util.List;

import static org.d3javu.bd.models.user.Roles.admin;

//@EnableMethodSecurity
//@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserService userService;

    @Value("${app.report.generator.port}")
    private int port;

    @Value("${app.report.generator.host}")
    private String host;

    @Autowired
    public SecurityConfiguration(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/login", "/registration").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/admin/**").hasAuthority(admin.getAuthority())
                                .requestMatchers("/posts/byids").permitAll()
                                .requestMatchers("/posts/byids/*").permitAll()
                                .requestMatchers("/posts/byids/**").permitAll()
                                .requestMatchers("/api/v1/posts").permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/users/{\\d}/delete")).hasRole(admin.getAuthority())
//                                .requestMatchers("/api/**").permitAll()
//                                .requestMatchers("/swagger-ui/**").permitAll()
                                .anyRequest().authenticated()
//                                .anyRequest().permitAll()
                )
//                .httpBasic(Customizer.withDefaults());
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/posts", false)
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login").deleteCookies("JSESSIONID").permitAll());
        return http.build();
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://" + this.host + ":" + this.port));
        configuration.setAllowedMethods(List.of("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new Argon2PasswordEncoder(16, 32, 1, 8192, 2);
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    public UserDetailsService userDetailsService() throws UsernameNotFoundException {
        return username -> {
            User user;

//            try{
                user = userService.findByEmail(username);
//            }catch (UsernameNotFoundException e){
//                throw new UsernameNotFoundException(username);
//            }
            if (user != null) {
                return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                        List.of(user.getRole()));
            }
            throw new NotFoundException("user not found");
        };
    }

//    @Bean
//    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
//        return (request, response, authentication) -> {
//            boolean isAdmin = authentication.getAuthorities().stream()
//                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
//            if (isAdmin) {
//                response.sendRedirect("/admin");
//            } else {
//                response.sendRedirect("/user");
//            }
//        };
//    }

}
