package com.yunes.taskmanager.controller;

import com.yunes.taskmanager.model.Task;
import com.yunes.taskmanager.model.TaskPriority;
import com.yunes.taskmanager.model.TaskStatus;
import com.yunes.taskmanager.repository.TaskRepository;
import com.yunes.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public HomeController(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(required = false) TaskStatus status,
            Model model
    ) {
        Task newTask = new Task();
        newTask.setPriority(TaskPriority.MEDIUM);

        addHomePageData(model, status, newTask);
        return "index";
    }

    @PostMapping("/tasks")
    public String createTask(
            @Valid @ModelAttribute("newTask") Task task,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            addHomePageData(model, null, task);
            return "index";
        }

        taskService.createTask(task);
        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/complete")
    public String completeTask(@PathVariable Long id) {
        taskService.completeTask(id);
        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        Task task = taskService.findTaskById(id);
        taskRepository.delete(task);
        return "redirect:/";
    }

    @GetMapping("/tasks/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Task task = taskService.findTaskById(id);

        model.addAttribute("task", task);
        model.addAttribute("priorities", TaskPriority.values());
        model.addAttribute("statuses", TaskStatus.values());

        return "edit-task";
    }

    @PostMapping("/tasks/{id}/edit")
    public String updateTask(
            @PathVariable Long id,
            @Valid @ModelAttribute("task") Task task,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("priorities", TaskPriority.values());
            model.addAttribute("statuses", TaskStatus.values());
            return "edit-task";
        }

        taskService.updateTask(id, task);
        return "redirect:/";
    }

    private void addHomePageData(Model model, TaskStatus status, Task newTask) {
        if (status == null) {
            model.addAttribute("tasks", taskService.findAllTasks());
        } else {
            model.addAttribute("tasks", taskService.findTasksByStatus(status));
        }

        model.addAttribute("newTask", newTask);
        model.addAttribute("priorities", TaskPriority.values());
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("selectedStatus", status);
    }
}