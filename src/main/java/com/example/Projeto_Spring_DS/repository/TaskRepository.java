package com.example.Projeto_Spring_DS.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.Projeto_Spring_DS.model.Task;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Repository
@NoArgsConstructor
@AllArgsConstructor
public class TaskRepository {
    private List<Task> tasks = new ArrayList<>();

    public Task addTask(Task task){
        this.tasks.add(task);
        return task;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
