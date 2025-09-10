package com.taskwebsite.repository;

import com.taskwebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository - Data Access Layer
 * 
 * Repository interface for User entity operations.
 * Extends JpaRepository to provide basic CRUD operations.
 * 
 * Custom methods:
 * - findByUsername: Find user by username (email)
 * - findByEmail: Find user by email address
 * - existsByUsername: Check if username exists
 * - existsByEmail: Check if email exists
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username (email address)
     * Used for authentication during sign-in
     * 
     * @param username The username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email address
     * Used for validation during sign-up
     * 
     * @param email The email address to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if username already exists
     * Used for validation during sign-up
     * 
     * @param username The username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email already exists
     * Used for validation during sign-up
     * 
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);
}