package com.example.Projeto_Spring_DS.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Projeto_Spring_DS.model.Task;
import com.example.Projeto_Spring_DS.service.TaskService;

@RestController
@RequestMapping("/tasks")
class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public Task createTrask(@RequestBody Task task){
        return taskService.addTask(task);
    }

    @GetMapping("/all")
    public List<Task> allTasks(){
        return taskService.allTasks();
    }

    @GetMapping("/{id}")
    public Task taskById(@PathVariable int id){
        return taskService.getTaskById(id);
    }

    @PutMapping("/update-{id}")
    public Task update(@PathVariable int id, @RequestBody Task taskDetails){
        return taskService.updateTask(id, taskDetails);
    }
}