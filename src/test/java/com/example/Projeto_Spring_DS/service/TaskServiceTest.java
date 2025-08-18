package com.example.Projeto_Spring_DS.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.Projeto_Spring_DS.dto.TaskRequestDTO;
import com.example.Projeto_Spring_DS.dto.TaskResponseDTO;
import com.example.Projeto_Spring_DS.model.Task;
import com.example.Projeto_Spring_DS.repository.TaskRepository;

@SpringBootTest
@ActiveProfiles("test")
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void shouldSaveTaskAndReturnDtoWithCorrectIdAndData() {
        // Arrange
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        taskRequest.setTitle("Test Task");
        taskRequest.setDescription("This is a test task");
        taskRequest.setDueDate(LocalDateTime.now().plusDays(1));

        // Act
        TaskResponseDTO response = taskService.addTask(taskRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Test Task", response.getTitle());
        assertEquals("This is a test task", response.getDescription());
        assertFalse(response.isCompleted());

        // DB verification
        Task persisted = taskRepository.findById(response.getId()).orElseThrow();
        assertNotNull(persisted);
        assertEquals("Test Task", persisted.getTitle());
        assertEquals("This is a test task", persisted.getDescription());
        assertFalse(persisted.isCompleted());
    }

    @Test
    void shouldUpdateTaskAndReturnDtoWithCorrectData() {
        // Arrange
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        taskRequest.setTitle("Test Task");
        taskRequest.setDescription("This is a test task");
        taskRequest.setDueDate(LocalDateTime.now().plusDays(1));

        TaskResponseDTO createdTask = taskService.addTask(taskRequest);

        TaskRequestDTO updateRequest = new TaskRequestDTO();
        updateRequest.setTitle("Updated Task");
        updateRequest.setDescription("This is an updated test task");
        updateRequest.setDueDate(LocalDateTime.now().plusDays(2));

        // Act
        TaskResponseDTO response = taskService.updateTask(createdTask.getId(), updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(createdTask.getId(), response.getId());
        assertEquals("Updated Task", response.getTitle());
        assertEquals("This is an updated test task", response.getDescription());
        assertFalse(response.isCompleted());

        // DB verification
        Task persisted = taskRepository.findById(response.getId()).orElseThrow();
        assertNotNull(persisted);
        assertEquals("Updated Task", persisted.getTitle());
        assertEquals("This is an updated test task", persisted.getDescription());
        assertFalse(persisted.isCompleted());
    }

    @Test
    void shouldDeleteTaskAndRemoveFromDatabase() {
        // Arrange
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        taskRequest.setTitle("Test Task");
        taskRequest.setDescription("This is a test task");
        taskRequest.setDueDate(LocalDateTime.now().plusDays(1));

        TaskResponseDTO createdTask = taskService.addTask(taskRequest);

        // Act
        taskService.deleteTask(createdTask.getId());

        // Assert
        assertFalse(taskRepository.findById(createdTask.getId()).isPresent());
    }
}