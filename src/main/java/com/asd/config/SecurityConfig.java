package com.asd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF enabled — Thymeleaf th:action auto-injects the token in all forms
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**") // REST endpoints use session guard, not CSRF tokens
                )
                // Authentication is enforced per-controller via session checks (requireLogin).
                // Public paths: login page, static assets, and the login POST action.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .anyRequest().permitAll() // session guards in controllers handle the rest
                )
                .formLogin(form -> form.disable()); // using custom AuthController

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}






