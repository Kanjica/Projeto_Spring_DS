package com.example.Projeto_Spring_DS.service;
import com.example.Projeto_Spring_DS.dto.TaskRequestDTO;
import com.example.Projeto_Spring_DS.dto.TaskResponseDTO;
import com.example.Projeto_Spring_DS.exception.ResourceNotFoundException;
import com.example.Projeto_Spring_DS.model.Task;
import com.example.Projeto_Spring_DS.repository.TaskRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public Task toEntity(TaskRequestDTO taskRequestDTO) {
        Task task = new Task();
        task.setId(taskRequestDTO.getId());
        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        task.setDueDate(taskRequestDTO.getDueDate());
        return task;
    }

    public TaskResponseDTO toResponseDTO(Task task) {
        return new TaskResponseDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.isCompleted(),
            task.getDueDate()
        );
    }
    
    public TaskResponseDTO addTask(TaskRequestDTO requestDTO){
        Task task = toEntity(requestDTO);
        Task savedTask = taskRepository.addTask(task);
        return toResponseDTO(savedTask);
    }

    public List<TaskResponseDTO> allTasks(){
        return taskRepository.getTasks().stream()
            .map(this::toResponseDTO)
            .toList();
    }

    public TaskResponseDTO getTaskById(int id) {
        Task task = taskRepository.getTaskById(id);
        return toResponseDTO(task);
    }

    public TaskResponseDTO updateTask(int id, TaskRequestDTO taskDetails) {
        Task task = taskRepository.getTaskById(id);
        setTask(task, toEntity(taskDetails));
        return toResponseDTO(task);
    }
    
    private Task setTask(Task task1, Task task2){
        task1.setTitle(task2.getTitle());
        task1.setDescription(task2.getDescription());
        task1.setCompleted(task2.isCompleted());
        task1.setDueDate(task2.getDueDate());
        return task1;
    }

    public void deleteTask(int id) {
        int index = id - 1;
        
        if (index < 0 || index >= taskRepository.getTasks().size()) {
            throw new ResourceNotFoundException("Tarefa com ID " + id + " não encontrada.");
        }
        taskRepository.getTasks().remove(index);
    }
}