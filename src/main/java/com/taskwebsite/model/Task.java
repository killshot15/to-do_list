package com.taskwebsite.model;

import jakarta.persistence.*;

/**
 * Task Entity - Data Access Layer
 * 
 * Represents a task in the task management system.
 * Maps to the 'tasks' table in SQLite database.
 * 
 * Fields:
 * - id: Primary key, auto-generated
 * - title: Task title/description
 * - userId: Foreign key reference to User (mapped via user relationship)
 * - user: Many-to-one relationship with User entity
 */
@Entity
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    // Default constructor
    public Task() {}
    
    // Constructor with parameters
    public Task(String title, Long userId) {
        this.title = title;
        this.userId = userId;
    }
    
    // Constructor with user
    public Task(String title, User user) {
        this.title = title;
        this.user = user;
        this.userId = user.getId();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getId();
        }
    }
    
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", userId=" + userId +
                '}';
    }
}