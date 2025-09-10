package com.taskwebsite.config;

import com.taskwebsite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig - Security Configuration
 * 
 * Configures Spring Security for the task management application:
 * - Form-based authentication with custom login page
 * - BCrypt password encoding
 * - Route protection (tasks and API endpoints require authentication)
 * - Custom login/logout behavior
 * - Remember me functionality (placeholder)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * BCrypt password encoder bean
     * Used for encoding passwords during registration and authentication
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Security filter chain configuration
     * Defines which routes are protected and authentication behavior
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserService userService) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Public routes - no authentication required
                .requestMatchers("/sign-in", "/sign-up", "/css/**", "/js/**", "/images/**").permitAll()
                // Protected routes - authentication required
                .requestMatchers("/tasks", "/api/tasks/**").authenticated()
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                // Custom login page
                .loginPage("/sign-in")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                // Redirect to tasks page on successful login
                .defaultSuccessUrl("/tasks", true)
                // Redirect to sign-in with error on failure
                .failureUrl("/sign-in?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/sign-in?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .rememberMe(remember -> remember
                .key("taskWebsiteRememberMe")
                .tokenValiditySeconds(86400) // 24 hours
                .userDetailsService(userService)
            )
            // Disable CSRF for API endpoints (for simplicity)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            )
            // Handle access denied
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/sign-in?error=access_denied")
            );
            
        return http.build();
    }
    
    /**
     * Authentication manager configuration
     * Configures user details service and password encoder
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UserService userService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
            .userDetailsService(userService)
            .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}