package com.app.peach.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Step 1: Read Authorization header
        String authHeader = request.getHeader("Authorization");

        // Step 2: Check if header exists and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // Step 3: Extract token
            String token = authHeader.substring(7);

            // Step 4: Validate token
            if (JwtUtil.isValid(token)) {

                // Step 5: Extract userId from token
                UUID userId = JwtUtil.extractUserId(token);

                // Step 6: Tell Spring "this user is authenticated"
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId,     // principal (who)
                                null,       // credentials (not needed)
                                Collections.emptyList() // roles (later)
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        // Step 7: Continue request
        filterChain.doFilter(request, response);
//        System.out.println("Authorization header: " + authHeader);
    }
}