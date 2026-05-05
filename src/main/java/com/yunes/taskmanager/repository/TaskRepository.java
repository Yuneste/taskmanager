package com.yunes.taskmanager.repository;

import com.yunes.taskmanager.model.Task;
import com.yunes.taskmanager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
}
