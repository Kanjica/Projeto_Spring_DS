package com.example.Projeto_Spring_DS.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.Projeto_Spring_DS.dto.TaskRequestDTO;
import com.example.Projeto_Spring_DS.dto.TaskResponseDTO;
import com.example.Projeto_Spring_DS.exception.ResourceNotFoundException;
import com.example.Projeto_Spring_DS.model.Task;
import com.example.Projeto_Spring_DS.repository.TaskRepository;

@SpringBootTest
class TaskServiceTest {

    @Mock
    private TaskRepository repository;
    
    @InjectMocks
    private TaskService service;

    @Test
    public void createTaskWithSuccess(){
        TaskRequestDTO request = new TaskRequestDTO("Task1", "Description1", LocalDateTime.now());

        Task savedTask = new Task(1L, "Task1", "Description1", false, LocalDateTime.now());

        when(repository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponseDTO task = service.addTask(request);

        assertNotNull(task);
        assertEquals(savedTask.getId(), task.getId());
        assertEquals(savedTask.getTitle(), task.getTitle());
        assertEquals(savedTask.getDescription(), task.getDescription());
        assertEquals(savedTask.isCompleted(), task.isCompleted());
    }

    @Test
    public void listAllTasksWithSucces(){
        when(repository.findAll()).thenReturn(
            List.of(
                new Task(1L, "Task1", "Description1", false, LocalDateTime.now()),
                new Task(2L, "Task2", "Description2", false, LocalDateTime.now().plusDays(1))
            )
        );

        List<TaskResponseDTO> tasks = service.listAllTasks();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());

        assertEquals(1L, tasks.get(0).getId());
        assertEquals("Task1", tasks.get(0).getTitle());
        assertEquals("Description1", tasks.get(0).getDescription());
        assertEquals(false, tasks.get(0).isCompleted());
        assertNotNull(tasks.get(0).getDueDate());

        assertEquals(2L, tasks.get(1).getId());
        assertEquals("Task2", tasks.get(1).getTitle());
        assertEquals("Description2", tasks.get(1).getDescription());
        assertEquals(false, tasks.get(1).isCompleted());
        assertNotNull(tasks.get(1).getDueDate());
    }

    @Test
    void getTaskByIdWithSuccess(){
        Task existingTask = new Task(1L, "Task1", "Description1", false, LocalDateTime.now());
        when(repository.findById(1L)).thenReturn(Optional.of(existingTask));

        TaskResponseDTO response = service.getTaskById(1L);

        assertNotNull(response);
        assertEquals(existingTask.getId(), response.getId());
        assertEquals(existingTask.getTitle(), response.getTitle());
        assertEquals(existingTask.getDescription(), response.getDescription());
        assertEquals(existingTask.isCompleted(), response.isCompleted());
    }

    @Test
    void getTaskByIdNotFound(){
        when(repository.findById(1L)).thenThrow(new ResourceNotFoundException("Tarefa com ID 1 não encontrada."));

        try {
            service.getTaskById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Tarefa com ID 1 não encontrada.", e.getMessage());
        }
    }

    @Test
    void updateTaskWithSuccess(){
        Task existingTask = new Task(1L, "Task1", "Description1", false, LocalDateTime.now());

        TaskRequestDTO updateRequest = new TaskRequestDTO("Updated Task", "Updated Description", LocalDateTime.now().plusDays(2));

        when(repository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(repository.save(any(Task.class))).thenReturn(existingTask); 

        TaskResponseDTO updatedTask = service.updateTask(1L, updateRequest);

        assertNotNull(updatedTask);
        assertEquals(existingTask.getId(), updatedTask.getId());
        assertEquals(updateRequest.getTitle(), updatedTask.getTitle());
        assertEquals(updateRequest.getDescription(), updatedTask.getDescription());
    }

    @Test
    void updateTaskWithIdNotFound(){
        TaskRequestDTO updateRequest = new TaskRequestDTO("Task1", "Description1",LocalDateTime.now()); 
        when(repository.findById(1L)).thenThrow(new ResourceNotFoundException("Tarefa com ID 1 não encontrada."));
        try {
            service.updateTask(1L, updateRequest);
        } catch (ResourceNotFoundException e) {
            assertEquals("Tarefa com ID 1 não encontrada.", e.getMessage());
        }   
    }

    @Test
    void deleteTaskWithSuccess(){
        Task existingTask = new Task(1L, "Task1", "Description1", false, LocalDateTime.now());
        when(repository.findById(1L)).thenReturn(Optional.of(existingTask));

        service.deleteTask(1L);

        verify(repository).delete(existingTask);
    }

    @Test
    void deleteTaskWithIdNotFound(){
        when(repository.findById(1L)).thenThrow(new ResourceNotFoundException("Tarefa com ID 1 não encontrada."));
        try {
            service.deleteTask(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Tarefa com ID 1 não encontrada.", e.getMessage());
        }
    }
}