package com.example.Projeto_Spring_DS.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Projeto_Spring_DS.model.Task;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<List<Task>> findByCompleted(boolean completed);
    Optional<List<Task>> findByDueDateBefore(LocalDateTime date);
    Optional<Task> findByTitle(String title);
}

