package com.app.peach.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF for development
                .csrf().disable()

                // Allow H2 console to be displayed in a frame
                .headers().frameOptions().disable()
                .and()

                // Authorization rules
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll();

        return http.build();
    }
}