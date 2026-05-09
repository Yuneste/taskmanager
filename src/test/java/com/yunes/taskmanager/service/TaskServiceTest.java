package com.yunes.taskmanager.service;

import com.yunes.taskmanager.model.Task;
import com.yunes.taskmanager.model.TaskPriority;
import com.yunes.taskmanager.model.TaskStatus;
import com.yunes.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void findAllTasksReturnsAllTasks() {
        Task task1 = new Task(
                "Study Java",
                "Practice OOP",
                TaskPriority.HIGH,
                LocalDate.now().plusDays(1)
        );

        Task task2 = new Task(
                "Learn Spring Boot",
                "Build a web app",
                TaskPriority.MEDIUM,
                LocalDate.now().plusDays(2)
        );

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> result = taskService.findAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository).findAll();
    }

    @Test
    void findTasksByStatusReturnsFilteredTasks() {
        Task task = new Task(
                "Finish project",
                "Complete the task manager",
                TaskPriority.HIGH,
                LocalDate.now().plusDays(1)
        );

        when(taskRepository.findByStatus(TaskStatus.OPEN)).thenReturn(List.of(task));

        List<Task> result = taskService.findTasksByStatus(TaskStatus.OPEN);

        assertEquals(1, result.size());
        assertEquals("Finish project", result.get(0).getTitle());
        verify(taskRepository).findByStatus(TaskStatus.OPEN);
    }

    @Test
    void createTaskSavesTask() {
        Task task = new Task(
                "Write tests",
                "Add service tests",
                TaskPriority.MEDIUM,
                LocalDate.now().plusDays(1)
        );

        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);

        assertEquals("Write tests", result.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    void findTaskByIdReturnsTaskWhenFound() {
        Task task = new Task(
                "Read docs",
                "Read Spring documentation",
                TaskPriority.LOW,
                LocalDate.now().plusDays(1)
        );

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.findTaskById(1L);

        assertEquals("Read docs", result.getTitle());
        verify(taskRepository).findById(1L);
    }

    @Test
    void findTaskByIdThrowsExceptionWhenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.findTaskById(99L)
        );

        assertEquals("Task not found", exception.getMessage());
        verify(taskRepository).findById(99L);
    }

    @Test
    void completeTaskChangesStatusToCompleted() {
        Task task = new Task(
                "Complete task",
                "Mark this task as done",
                TaskPriority.HIGH,
                LocalDate.now().plusDays(1)
        );

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.completeTask(1L);

        assertEquals(TaskStatus.COMPLETED, task.getStatus());
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task);
    }

    @Test
    void deleteTaskDeletesById() {
        taskService.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }
}