package com.taskwebsite.service;

import com.taskwebsite.model.User;
import com.taskwebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * UserService - Business Logic Layer
 * 
 * Service class for user-related operations including:
 * - User registration with validation
 * - User authentication for Spring Security
 * - Password encoding with BCrypt
 * - Email validation
 * 
 * Implements UserDetailsService for Spring Security integration
 */
@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    // Email validation regex pattern
    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    /**
     * Register a new user with validation
     * 
     * @param fullName User's full name
     * @param email User's email address (used as username)
     * @param password Plain text password
     * @param confirmPassword Password confirmation
     * @return Success message or error message
     */
    public String registerUser(String fullName, String email, String password, String confirmPassword) {
        // Validate input
        if (fullName == null || fullName.trim().isEmpty()) {
            return "Full name is required";
        }
        
        if (!isValidEmail(email)) {
            return "Please enter a valid email address";
        }
        
        if (password == null || password.length() < 8) {
            return "Password must be at least 8 characters";
        }
        
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            return "Email already exists";
        }
        
        // Create new user
        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email.toLowerCase());
        user.setUsername(email.toLowerCase()); // Use email as username
        user.setPassword(passwordEncoder.encode(password));
        
        try {
            userRepository.save(user);
            return "SUCCESS";
        } catch (Exception e) {
            return "Registration failed. Please try again.";
        }
    }
    
    /**
     * Validate email format using regex
     * 
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        if (email == null) return false;
        return pattern.matcher(email).matches();
    }
    
    /**
     * Find user by username (email)
     * 
     * @param username Username to search for
     * @return User if found, null otherwise
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    
    /**
     * Check if user credentials are valid
     * 
     * @param username Username (email)
     * @param password Plain text password
     * @return true if valid, false otherwise
     */
    public boolean validateUser(String username, String password) {
        User user = findByUsername(username);
        if (user != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
    
    /**
     * Load user by username for Spring Security
     * Required by UserDetailsService interface
     * 
     * @param username Username to load
     * @return UserDetails object for Spring Security
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>() // No roles/authorities for this simple application
        );
    }
}