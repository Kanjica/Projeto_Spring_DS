package com.example.Projeto_Spring_DS.service;
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

    public Task addTask(Task task){
        return taskRepository.addTask(task);
    }

    public List<Task> allTasks(){
        return taskRepository.getTasks();
    }

    public Task getTaskById(int id){
        if(id <= 0 || id > taskRepository.getTasks().size()){
            throw new ResourceNotFoundException("Tarefa com ID " + id + "não existe.");
        }        
        return taskRepository.getTasks().get(id-1);
    }

    public Task updateTask(int id, Task taskDetails) {
        int index = id - 1;
        
        if (index < 0 || index >= taskRepository.getTasks().size()) {
            throw new ResourceNotFoundException("Tarefa com ID " + id + " não encontrada.");
        }
        Task existingTask = taskRepository.getTasks().get(index);
        setTask(existingTask, taskDetails);
        return existingTask;
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