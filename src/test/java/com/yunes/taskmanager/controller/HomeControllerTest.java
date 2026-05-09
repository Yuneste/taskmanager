package com.yunes.taskmanager.controller;

import com.yunes.taskmanager.model.Task;
import com.yunes.taskmanager.model.TaskPriority;
import com.yunes.taskmanager.repository.TaskRepository;
import com.yunes.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    void homePageLoadsAndShowsTasks() throws Exception {
        Task task = new Task(
                "Study Spring MVC",
                "Learn controllers and views",
                TaskPriority.HIGH,
                LocalDate.now().plusDays(1)
        );

        when(taskService.findAllTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("Study Spring MVC")));
    }
}