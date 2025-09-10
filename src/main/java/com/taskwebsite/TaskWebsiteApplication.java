package com.taskwebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application class for Task Management Website
 * 
 * This application provides:
 * - User authentication and registration
 * - Task management with CRUD operations
 * - Thymeleaf-based web interface
 * - SQLite database integration
 * - Spring Security for authentication
 */
@SpringBootApplication
public class TaskWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskWebsiteApplication.class, args);
    }
}