package com.yunes.taskmanager.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a task stored in the database.
 *
 * This class is marked with @Entity, so JPA/Hibernate treats it as
 * a database entity. Each object of this class represents one row in the
 * Task table.
 *
 * The id field is the primary key. It is annotated with @Id and
 * @GeneratedValue, which means the database generates the ID automatically
 * when a new task is saved. The GenerationType.IDENTITY strategy uses the
 * database's auto-increment behavior, so IDs are created in order, for example
 * 1, 2, 3, and so on.
 *
 * The task's priority- & status fields are enums. They are stored as strings in the
 * database because of @Enumerated(EnumType.STRING). This means values such as
 * OPEN or HIGH are stored as text instead of numbers.
 *
 * The dueDate field stores the date when the task should be done.
 * The createdAt field stores the date and time when the task was created.
 *
 * The no-argument constructor is required by JPA/Hibernate. The second
 * constructor is a convenience constructor for creating a task with the main
 * fields already set. It also sets the default status to OPEN and sets
 * createdAt to the current date and time.
 *
 * The onCreate() method is annotated with @PrePersist, so Hibernate calls it
 * automatically right before a new task is inserted into the database. It makes
 * sure that a new task ALWAYS has a default status of OPEN and a createdAt
 * timestamp, even if the empty constructor was used and those fields were not
 * set manually.
 *
 * OPEN means that the task is newly created or still active and has not been
 * completed yet.
 */

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDate dueDate;

    private LocalDateTime createdAt;

    public Task() {
    }

    public Task(String title, String description, TaskPriority priority, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = TaskStatus.OPEN;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void onCreate() {
        if (this.status == null) {
            this.status = TaskStatus.OPEN;
        }

        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}