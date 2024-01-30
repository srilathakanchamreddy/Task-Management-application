package com.example.taskmanagement.Controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.taskmanagement.Exceptions.TaskNotFoundException;
import com.example.taskmanagement.Exceptions.TaskValidationException;
import com.example.taskmanagement.Models.Task;
import com.example.taskmanagement.Services.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    public void testGetAllTasks() throws Exception {
        // Mocking the behavior of the service
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(new Task(), new Task()));

        // Performing a mock HTTP request and verifying the response
        mockMvc.perform(get("/tasks/getAllTasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetTaskById() throws Exception {
        // Mocking the behavior of the service
        when(taskService.getTaskById(1L)).thenReturn(new Task());

        // Performing a mock HTTP request and verifying the response
        mockMvc.perform(get("/tasks/getTaskById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void testGetTaskByIdNotFound() throws Exception {
        // Mocking the behavior of the service
        when(taskService.getTaskById(1L)).thenThrow(new TaskNotFoundException("Task not found with id: 1"));

        // Performing a mock HTTP request and verifying the response
        mockMvc.perform(get("/tasks/getTaskById/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Task not found with id: 1"));
    }

    @Test
    public void testCreateTask() throws Exception {
        // Mocking the behavior of the service
        Task createdTask = new Task();
        when(taskService.createTask(any())).thenReturn(createdTask);

        // Performing a mock HTTP request and verifying the response
        mockMvc.perform(post("/tasks/createTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateTaskInvalidInput() throws Exception {
        // Mocking the behavior of the service
        List<String> errors = Arrays.asList("Title cannot be blank", "Description cannot be blank");
        when(taskService.createTask(any())).thenThrow(new TaskValidationException(errors));

        // Performing a mock HTTP request and verifying the response
        mockMvc.perform(post("/tasks/createTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(3)));

    }


    @Test
    public void testUpdateTaskInvalidInput() throws Exception {
        // Mocking the behavior of the service
        List<String> errors = Arrays.asList("Title cannot be blank", "Description cannot be blank");
        when(taskService.updateTask(eq(1L), any())).thenThrow(new TaskValidationException(errors));

        // Performing a mock HTTP request and verifying the response
        mockMvc.perform(put("/tasks/updateTask/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(3)));

    }

    @Test
    public void testUpdateTaskNotFound() throws Exception {
        String requestContent = "{"
                + "\"title\": \"Updated Title\","
                + "\"description\": \"Updated Description\","
                + "\"dueDate\": \"2022-01-25\""
                + "}";
        // Mocking the behavior of the service
        when(taskService.updateTask(eq(1L), any())).thenThrow(new TaskNotFoundException("Task not found with id: 1"));

        // Performing a mock HTTP request and verifying the response
        mockMvc.perform(put("/tasks/updateTask/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Task not found with id: 1"));
    }

    @Test
    public void testDeleteTask() throws Exception {
        // Mocking the behavior of the service
        doNothing().when(taskService).deleteTask(1L);

        // Performing a mock HTTP request and verifying the response
        mockMvc.perform(delete("/tasks/deleteTask/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully"));
    }

    @Test
    public void testDeleteTaskNotFound() throws Exception {
        // Mocking the behavior of the service
        doThrow(new TaskNotFoundException("Task not found with id: 1")).when(taskService).deleteTask(1L);

        // Performing a mock HTTP request and verifying the response
        mockMvc.perform(delete("/tasks/deleteTask/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Task not found with id: 1"));
    }
}

