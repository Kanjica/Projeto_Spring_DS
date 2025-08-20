package com.example.Projeto_Spring_DS.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.Projeto_Spring_DS.dto.TaskRequestDTO;
import com.example.Projeto_Spring_DS.dto.TaskResponseDTO;
import com.example.Projeto_Spring_DS.exception.ResourceNotFoundException;
import com.example.Projeto_Spring_DS.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTaskWithSuccess() throws Exception {
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        taskRequest.setTitle("New Task");
        taskRequest.setDescription("Task Description");
        taskRequest.setDueDate(LocalDateTime.now().plusDays(1));

        TaskResponseDTO responseDTO = new TaskResponseDTO(
            1L, 
            "New Task", 
            "Task Description",
            false, 
            LocalDateTime.now().plusDays(1)
        );

        when(taskService.addTask(any(TaskRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Task"));
    }

    @Test
    void createTaskWithInvalidData() throws Exception {
        TaskRequestDTO invalidTaskRequest = new TaskRequestDTO();
        invalidTaskRequest.setTitle(null); 
        invalidTaskRequest.setDescription("Task Description");
        invalidTaskRequest.setDueDate(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTaskRequest)))
                .andExpect(status().isBadRequest()); 
    }
    
    @Test
    void listAllTasks() throws Exception{
        List<TaskResponseDTO> list = List.of(
            new TaskResponseDTO(1L, 
                "Task1", 
                "Description1", 
                false, 
                LocalDateTime.now()),

            new TaskResponseDTO(2L, 
                "Task2", 
                "Description2", 
                false, 
                LocalDateTime.now().plusDays(1))
        );

        when(taskService.listAllTasks()).thenReturn(list);

        mockMvc.perform(get("/tasks"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2)) 
               .andExpect(jsonPath("$[0].title").value("Task1")); 
    }

    @Test
    void getTaskByIdWithSucess() throws Exception{

        Long taskId = 1L; // Usando uma variável para o ID
        TaskResponseDTO response = new TaskResponseDTO(
            taskId,
            "Task",
            "Description",
            false,
            LocalDateTime.now()
        );
        
        when(taskService.getTaskById(taskId)).thenReturn(response);

        mockMvc.perform(get("/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.description").value(response.getDescription()));
    }

    @Test
    void getTaskByIdWithInvalidData() throws Exception {
        Long notFoundId = 1L; 
        
        when(taskService.getTaskById(notFoundId)).thenThrow(new ResourceNotFoundException("Tarefa com ID " + notFoundId + " não encontrada."));
        
        mockMvc.perform(get("/tasks/{id}", notFoundId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Tarefa com ID " + notFoundId + " não encontrada."))
                .andExpect(jsonPath("$.status").value(404));
    }   

    @Test
    void updateTaskWithSuccess() throws Exception {
        Long taskId = 1L;
        TaskRequestDTO updatedDetails = new TaskRequestDTO();
        updatedDetails.setTitle("Updated Title");
        updatedDetails.setDescription("Updated Description");
        updatedDetails.setDueDate(LocalDateTime.now().plusDays(2));

        TaskResponseDTO responseDTO = new TaskResponseDTO(
            taskId,
            "Updated Title",
            "Updated Description",
            true,
            LocalDateTime.now().plusDays(2)
        );

        when(taskService.updateTask(eq(taskId), any(TaskRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    void updateTaskWithNotFoundId() throws Exception {
        Long notFoundId = 99L;
        TaskRequestDTO updatedDetails = new TaskRequestDTO();
        updatedDetails.setTitle("Updated Title");

        when(taskService.updateTask(eq(notFoundId), any(TaskRequestDTO.class)))
            .thenThrow(new ResourceNotFoundException("Task with ID " + notFoundId + " not found."));

        mockMvc.perform(put("/tasks/{id}", notFoundId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTaskWithSuccess() throws Exception {
        Long taskId = 1L;

        doNothing().when(taskService).deleteTask(taskId);

        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isNoContent()); 
    }

    @Test
    void deleteTaskWithNotFoundId() throws Exception {
        Long notFoundId = 99L;

        doThrow(new ResourceNotFoundException("Task with ID " + notFoundId + " not found."))
            .when(taskService).deleteTask(notFoundId);

        mockMvc.perform(delete("/tasks/{id}", notFoundId))
                .andExpect(status().isNotFound());
    }
}
