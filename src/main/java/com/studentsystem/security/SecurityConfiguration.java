package com.studentsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentsystem.repository.UserRepository;
import com.studentsystem.dto.response.ErrorResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> userRepository.findByEmail(email)
            .map(user -> User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        http.csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((request, response, ex) -> {
                    response.setStatus(401);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    ErrorResponse errorResponse = buildErrorResponse(
                        "AUTHENTICATION_REQUIRED",
                        "Authentication credentials are required to access this resource",
                        401,
                        request.getRequestURI()
                    );
                    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                })
                .accessDeniedHandler((request, response, ex) -> {
                    response.setStatus(403);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    ErrorResponse errorResponse = buildErrorResponse(
                        "ACCESS_DENIED",
                        "You do not have permission to access this resource",
                        403,
                        request.getRequestURI()
                    );
                    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                }))
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/api/v1/admin/*").hasAnyRole("ADMIN")
                .requestMatchers("/api/v1/chancellor/*").hasAnyRole("CHANCELLOR")
                .requestMatchers("/api/v1/teacher/*").hasAnyRole("TEACHER","CHANCELLOR")
                .requestMatchers("/api/v1/students/*").hasAnyRole("STUDENT")
                .requestMatchers("/api/v1/passport/token").permitAll()
                .requestMatchers("/api/v1/create").permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private ErrorResponse buildErrorResponse(String errorCode, String message, int status, String path) {
        return new ErrorResponse(errorCode, message, status, path);
    }
}
