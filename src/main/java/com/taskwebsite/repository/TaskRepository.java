package com.taskwebsite.repository;

import com.taskwebsite.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TaskRepository - Data Access Layer
 * 
 * Repository interface for Task entity operations.
 * Extends JpaRepository to provide basic CRUD operations.
 * 
 * Custom methods:
 * - findByUserId: Find all tasks for a specific user
 * - findByUserIdOrderByIdAsc: Find tasks for user ordered by ID
 * - countByUserId: Count tasks for a specific user
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    /**
     * Find all tasks for a specific user
     * Used to display user's tasks on the tasks page
     * 
     * @param userId The user ID to search for
     * @return List of tasks belonging to the user
     */
    List<Task> findByUserId(Long userId);
    
    /**
     * Find all tasks for a specific user ordered by ID (creation order)
     * Used to display user's tasks in consistent order
     * 
     * @param userId The user ID to search for
     * @return List of tasks belonging to the user, ordered by ID
     */
    List<Task> findByUserIdOrderByIdAsc(Long userId);
    
    /**
     * Count the number of tasks for a specific user
     * Used for statistics or pagination
     * 
     * @param userId The user ID to count tasks for
     * @return Number of tasks belonging to the user
     */
    long countByUserId(Long userId);
}