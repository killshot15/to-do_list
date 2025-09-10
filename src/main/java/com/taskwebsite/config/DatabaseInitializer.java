package com.taskwebsite.config;

import com.taskwebsite.model.User;
import com.taskwebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DatabaseInitializer - Database Initialization Component
 * 
 * Ensures the test user is created in the database on application startup.
 * This component implements CommandLineRunner to execute after the application context is loaded.
 * 
 * Creates test user:
 * - Username/Email: test@example.com
 * - Password: Test1234 (BCrypt encoded)
 * - Full Name: Test User
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if test user already exists
        if (!userRepository.existsByUsername("test@example.com")) {
            // Create test user
            User testUser = new User();
            testUser.setUsername("test@example.com");
            testUser.setEmail("test@example.com");
            testUser.setFullName("Test User");
            testUser.setPassword(passwordEncoder.encode("Test1234"));
            
            userRepository.save(testUser);
            System.out.println("✅ Test user created successfully!");
            System.out.println("   Username: test@example.com");
            System.out.println("   Password: Test1234");
        } else {
            System.out.println("✅ Test user already exists in database");
        }
    }
}