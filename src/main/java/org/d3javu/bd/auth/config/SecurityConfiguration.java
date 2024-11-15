package org.d3javu.bd.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.d3javu.bd.models.user.Roles.admin;

//@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/login", "/registration").permitAll()
                                .requestMatchers("/admin/**").hasRole(admin.getAuthority())
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/users/{\\d}/delete")).hasRole(admin.getAuthority())
                                .anyRequest().authenticated()
                )
//                .httpBasic(Customizer.withDefaults());
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/users").permitAll())
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login").deleteCookies("JSESSIONID"));
        return http.build();
    }

}
