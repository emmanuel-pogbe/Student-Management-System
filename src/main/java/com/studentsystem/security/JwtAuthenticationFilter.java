package com.studentsystem.security;

import com.studentsystem.repository.UserRepository;
import com.studentsystem.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Bearer token found, time to verify ig");
            String token = authHeader.substring(7);

            try {
                String email = jwtUtils.getValidatedAccessClaims(token).getSubject();
                userRepository.findByEmail(email).ifPresent(user -> {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole()))
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    System.out.println("JwtAuthenticationFilter Access token valid. SecurityContext set for username=" + user.getEmail());
                });
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
                System.out.println("JwtAuthenticationFilter Access token invalid/expired for path reason=" + ex.getClass().getSimpleName());
            }
        }

        filterChain.doFilter(request, response);
    }
}
