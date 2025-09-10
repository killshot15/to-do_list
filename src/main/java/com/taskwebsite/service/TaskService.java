package com.taskwebsite.service;

import com.taskwebsite.model.Task;
import com.taskwebsite.model.User;
import com.taskwebsite.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * TaskService - Business Logic Layer
 * 
 * Service class for task-related operations including:
 * - CRUD operations for tasks
 * - Task validation
 * - User-specific task management
 * 
 * All operations are scoped to the authenticated user for security
 */
@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    /**
     * Create a new task for a user
     * 
     * @param title Task title
     * @param user User who owns the task
     * @return Created task or null if creation failed
     */
    public Task createTask(String title, User user) {
        if (title == null || title.trim().isEmpty()) {
            return null;
        }
        
        if (user == null) {
            return null;
        }
        
        Task task = new Task();
        task.setTitle(title.trim());
        task.setUserId(user.getId());
        task.setUser(user);
        
        try {
            return taskRepository.save(task);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Get all tasks for a specific user
     * 
     * @param userId User ID
     * @return List of tasks belonging to the user
     */
    public List<Task> getUserTasks(Long userId) {
        if (userId == null) {
            return List.of();
        }
        return taskRepository.findByUserIdOrderByIdAsc(userId);
    }
    
    /**
     * Get a specific task by ID, ensuring it belongs to the user
     * 
     * @param taskId Task ID
     * @param userId User ID for security check
     * @return Task if found and belongs to user, null otherwise
     */
    public Task getTaskById(Long taskId, Long userId) {
        if (taskId == null || userId == null) {
            return null;
        }
        
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            // Security check: ensure task belongs to the user
            if (task.getUserId().equals(userId)) {
                return task;
            }
        }
        return null;
    }
    
    /**
     * Update a task title
     * 
     * @param taskId Task ID
     * @param newTitle New task title
     * @param userId User ID for security check
     * @return Updated task or null if update failed
     */
    public Task updateTask(Long taskId, String newTitle, Long userId) {
        if (newTitle == null || newTitle.trim().isEmpty()) {
            return null;
        }
        
        Task task = getTaskById(taskId, userId);
        if (task != null) {
            task.setTitle(newTitle.trim());
            try {
                return taskRepository.save(task);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
    
    /**
     * Delete a task
     * 
     * @param taskId Task ID
     * @param userId User ID for security check
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteTask(Long taskId, Long userId) {
        Task task = getTaskById(taskId, userId);
        if (task != null) {
            try {
                taskRepository.delete(task);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Count tasks for a user
     * 
     * @param userId User ID
     * @return Number of tasks
     */
    public long countUserTasks(Long userId) {
        if (userId == null) {
            return 0;
        }
        return taskRepository.countByUserId(userId);
    }
    
    /**
     * Validate task title
     * 
     * @param title Task title to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty() && title.trim().length() <= 255;
    }
}