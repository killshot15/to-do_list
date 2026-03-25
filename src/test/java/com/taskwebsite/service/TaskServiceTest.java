package com.taskwebsite.service;

import com.taskwebsite.model.Task;
import com.taskwebsite.model.User;
import com.taskwebsite.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test@example.com");
    }

    // --- createTask ---

    @Test
    void createTask_withValidInput_returnsTask() {
        Task saved = new Task("Buy groceries", testUser);
        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        Task result = taskService.createTask("Buy groceries", testUser);

        assertNotNull(result);
        assertEquals("Buy groceries", result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createTask_withNullTitle_returnsNull() {
        Task result = taskService.createTask(null, testUser);
        assertNull(result);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createTask_withEmptyTitle_returnsNull() {
        Task result = taskService.createTask("   ", testUser);
        assertNull(result);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createTask_withNullUser_returnsNull() {
        Task result = taskService.createTask("Some task", null);
        assertNull(result);
        verify(taskRepository, never()).save(any());
    }

    // --- isValidTitle ---

    @Test
    void isValidTitle_withNormalTitle_returnsTrue() {
        assertTrue(taskService.isValidTitle("Fix the bug"));
    }

    @Test
    void isValidTitle_withNullTitle_returnsFalse() {
        assertFalse(taskService.isValidTitle(null));
    }

    @Test
    void isValidTitle_withEmptyTitle_returnsFalse() {
        assertFalse(taskService.isValidTitle("  "));
    }

    @Test
    void isValidTitle_withTooLongTitle_returnsFalse() {
        String longTitle = "a".repeat(256);
        assertFalse(taskService.isValidTitle(longTitle));
    }

    // --- getTaskById security check ---

    @Test
    void getTaskById_taskBelongsToOtherUser_returnsNull() {
        Task task = new Task("Someone else's task", 99L); // owned by user 99
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // testUser has id=1, task belongs to user 99
        Task result = taskService.getTaskById(1L, testUser.getId());

        assertNull(result); // security check should block this
    }

    @Test
    void getTaskById_taskBelongsToUser_returnsTask() {
        Task task = new Task("My task", 1L); // owned by user 1
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L, 1L);

        assertNotNull(result);
    }

    // --- getUserTasks ---

    @Test
    void getUserTasks_withNullUserId_returnsEmptyList() {
        List<Task> result = taskService.getUserTasks(null);
        assertTrue(result.isEmpty());
        verify(taskRepository, never()).findByUserIdOrderByIdAsc(any());
    }
}
