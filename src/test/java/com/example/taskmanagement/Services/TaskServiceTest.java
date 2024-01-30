package com.example.taskmanagement.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.taskmanagement.Exceptions.TaskNotFoundException;
import com.example.taskmanagement.Models.Task;
import com.example.taskmanagement.Repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void testGetAllTasks() {
        // Mocking the behavior of the repository
        when(taskRepository.findAll()).thenReturn(Arrays.asList(new Task(), new Task()));

        // Testing the service method
        List<Task> tasks = taskService.getAllTasks();

        // Verifying the result
        assertEquals(2, tasks.size());
    }

    @Test
    public void testGetTaskById() {
        // Mocking the behavior of the repository
        when(taskRepository.findById(1L)).thenReturn(Optional.of(new Task()));

        // Testing the service method
        Task task = taskService.getTaskById(1L);

        // Verifying the result
        assertNotNull(task);
    }

    @Test
    public void testGetTaskByIdNotFound() {
        // Mocking the behavior of the repository
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Testing the service method
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    public void testCreateTask() {
        // Mocking the behavior of the repository
        Task originalTask = new Task();
        when(taskRepository.save(any(Task.class))).thenReturn(originalTask);

        // Testing the service method
        Task createdTask = taskService.createTask(new Task());

        // Verifying the result
        assertNotNull(createdTask);
        assertEquals(originalTask, createdTask);
    }

    @Test
    public void testUpdateTask() {
        // Mocking the behavior of the repository
        when(taskRepository.findById(1L)).thenReturn(Optional.of(new Task()));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Testing the service method
        Task updatedTask = taskService.updateTask(1L, new Task());

        // Verifying the result
        assertNotNull(updatedTask);
    }

    @Test
    public void testUpdateTaskNotFound() {
        // Mocking the behavior of the repository
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Testing the service method
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(1L, new Task()));
    }

    @Test
    public void testDeleteTask() {
        // Mocking the behavior of the repository
        when(taskRepository.findById(1L)).thenReturn(Optional.of(new Task()));

        // Testing the service method
        assertDoesNotThrow(() -> taskService.deleteTask(1L));
    }

    @Test
    public void testDeleteTaskNotFound() {
        // Mocking the behavior of the repository
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Testing the service method
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1L));
    }

    // Add more test cases for other service methods
}

