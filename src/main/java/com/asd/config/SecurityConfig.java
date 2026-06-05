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
                        .ignoringRequestMatchers("/api/**", "/v3/api-docs/**") // REST + Swagger endpoints
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login", "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/favicon.svg", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form.disable()); // using custom AuthController

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}






