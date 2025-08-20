package com.example.Projeto_Spring_DS;

import com.example.Projeto_Spring_DS.dto.TaskRequestDTO;
import com.example.Projeto_Spring_DS.dto.TaskResponseDTO;
import com.example.Projeto_Spring_DS.model.Task;
import com.example.Projeto_Spring_DS.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@Transactional
class TaskIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TaskRepository taskRepository; 

    @Test
    void shouldCreateTaskAndPersistInDatabase() {
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        taskRequest.setTitle("New Integration Task");
        taskRequest.setDescription("Task to be saved in the DB");
        taskRequest.setDueDate(LocalDateTime.now().plusDays(1));

        webTestClient.post()
                .uri("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(taskRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TaskResponseDTO.class)
                .consumeWith(response -> {
                    TaskResponseDTO responseBody = response.getResponseBody();
                    assertNotNull(responseBody.getId());
                    assertEquals("New Integration Task", responseBody.getTitle());
                });

        Task savedTask = taskRepository.findByTitle("New Integration Task").get();
        assertNotNull(savedTask);
        assertEquals("Task to be saved in the DB", savedTask.getDescription());
    }
}
