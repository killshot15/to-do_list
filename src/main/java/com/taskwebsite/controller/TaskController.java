package com.taskwebsite.controller;

import com.taskwebsite.model.Task;
import com.taskwebsite.model.User;
import com.taskwebsite.service.TaskService;
import com.taskwebsite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TaskController - API and Presentation Layer
 * 
 * Handles both web pages and API endpoints for task management:
 * 
 * Web endpoints:
 * - GET/POST /tasks: Display tasks page and handle form submissions
 * 
 * API endpoints (JSON):
 * - GET /api/tasks: List user's tasks
 * - POST /api/tasks: Create new task
 * - PUT /api/tasks/{id}: Update task
 * - DELETE /api/tasks/{id}: Delete task
 */
@Controller
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Display tasks page with user's tasks
     * Redirects to sign-in if not authenticated
     */
    @GetMapping("/tasks")
    public String showTasks(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/sign-in?error=access_denied";
        }
        
        // Get current user
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        if (user == null) {
            return "redirect:/sign-in?error=user_not_found";
        }
        
        // Get user's tasks
        List<Task> tasks = taskService.getUserTasks(user.getId());
        
        // Add data to model
        model.addAttribute("user", user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("taskCount", tasks.size());
        
        return "tasks";
    }
    
    /**
     * Handle task form submission from tasks page
     */
    @PostMapping("/tasks")
    public String createTask(
            @RequestParam("title") String title,
            Authentication authentication,
            Model model) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/sign-in?error=access_denied";
        }
        
        // Get current user
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        if (user == null) {
            return "redirect:/sign-in?error=user_not_found";
        }
        
        // Validate and create task
        if (title == null || title.trim().isEmpty()) {
            model.addAttribute("error", "Task title is required");
        } else {
            Task task = taskService.createTask(title, user);
            if (task == null) {
                model.addAttribute("error", "Failed to create task");
            } else {
                model.addAttribute("success", "Task created successfully");
            }
        }
        
        // Reload tasks and return to tasks page
        List<Task> tasks = taskService.getUserTasks(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("taskCount", tasks.size());
        
        return "tasks";
    }
    
    // ========== API ENDPOINTS ==========
    
    /**
     * API: Get all tasks for the authenticated user
     * GET /api/tasks
     */
    @GetMapping("/api/tasks")
    @ResponseBody
    public ResponseEntity<List<Task>> getTasks(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        List<Task> tasks = taskService.getUserTasks(user.getId());
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * API: Create a new task
     * POST /api/tasks
     */
    @PostMapping("/api/tasks")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createTaskApi(
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("error", "Authentication required");
            return ResponseEntity.status(401).body(response);
        }
        
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        if (user == null) {
            response.put("error", "User not found");
            return ResponseEntity.status(401).body(response);
        }
        
        String title = request.get("title");
        if (title == null || title.trim().isEmpty()) {
            response.put("error", "Task title is required");
            return ResponseEntity.badRequest().body(response);
        }
        
        Task task = taskService.createTask(title, user);
        if (task == null) {
            response.put("error", "Failed to create task");
            return ResponseEntity.status(500).body(response);
        }
        
        response.put("success", true);
        response.put("task", task);
        return ResponseEntity.ok(response);
    }
    
    /**
     * API: Update a task
     * PUT /api/tasks/{id}
     */
    @PutMapping("/api/tasks/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateTaskApi(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("error", "Authentication required");
            return ResponseEntity.status(401).body(response);
        }
        
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        if (user == null) {
            response.put("error", "User not found");
            return ResponseEntity.status(401).body(response);
        }
        
        String title = request.get("title");
        if (title == null || title.trim().isEmpty()) {
            response.put("error", "Task title is required");
            return ResponseEntity.badRequest().body(response);
        }
        
        Task task = taskService.updateTask(id, title, user.getId());
        if (task == null) {
            response.put("error", "Task not found or update failed");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("success", true);
        response.put("task", task);
        return ResponseEntity.ok(response);
    }
    
    /**
     * API: Delete a task
     * DELETE /api/tasks/{id}
     */
    @DeleteMapping("/api/tasks/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteTaskApi(
            @PathVariable Long id,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("error", "Authentication required");
            return ResponseEntity.status(401).body(response);
        }
        
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        if (user == null) {
            response.put("error", "User not found");
            return ResponseEntity.status(401).body(response);
        }
        
        boolean deleted = taskService.deleteTask(id, user.getId());
        if (!deleted) {
            response.put("error", "Task not found or delete failed");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("success", true);
        response.put("message", "Task deleted successfully");
        return ResponseEntity.ok(response);
    }
}