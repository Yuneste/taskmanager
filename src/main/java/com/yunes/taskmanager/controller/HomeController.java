package com.yunes.taskmanager.controller;

import com.yunes.taskmanager.model.Task;
import com.yunes.taskmanager.model.TaskPriority;
import com.yunes.taskmanager.model.TaskStatus;
import com.yunes.taskmanager.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final TaskService taskService;

    public HomeController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(required = false) TaskStatus status,
            Model model
    ) {
        if(status == null){
            model.addAttribute("tasks", taskService.findAllTasks());
        } else {
            model.addAttribute("tasks", taskService.findTasksByStatus(status));
        }
        model.addAttribute("newTask", new Task());
        model.addAttribute("priorities", TaskPriority.values());
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("selectedStatus", status);

        return "index";
    }

    @PostMapping("/tasks")
    public String createTask(@ModelAttribute Task task) {
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
        taskService.deleteTask(id);
        return "redirect:/";
    }
}
