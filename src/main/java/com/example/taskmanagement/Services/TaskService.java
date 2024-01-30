package com.example.taskmanagement.Services;

import com.example.taskmanagement.Exceptions.TaskNotFoundException;
import com.example.taskmanagement.Models.Task;
import com.example.taskmanagement.Repositories.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service

public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Operation(summary = "Retrieve all tasks", description = "Get a list of all tasks.")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    @Operation(summary = "Retrieve a task by ID", description = "Get details of a specific task by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public Task getTaskById(Long id) {
       if(taskRepository.findById(id).isPresent()){
           return taskRepository.findById(id).get();
         }
       else {
           throw new TaskNotFoundException("Task not found with id: " + id);
       }
    }

    public Task createTask(Task task) {
        task.setCreationDate(new Date());
        task.setLastUpdateDate(new Date());
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setDueDate(updatedTask.getDueDate());
            task.setLastUpdateDate(new Date());
            return taskRepository.save(task);
        } else {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
    }

    public void deleteTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            taskRepository.deleteById(id);
        } else {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
    }

    public List<Task> getTasksByAssignee(String assignee) {
        return taskRepository.findByAssignee(assignee);
    }

    public List<Task> getTasksByCategory(String category) {
        return taskRepository.findByCategory(category);
    }

    public List<Task> getTasksByTitle(String title) {
        return taskRepository.findByTitleContaining(title);
    }
    public List<Task> getTasksByDescription(String description) {
        return taskRepository.findByDescriptionContaining(description);
    }
    public List<Task> getTasksByDueDate(Date dueDateStart, Date dueDateEnd) {
        return taskRepository.findByDueDateBetween(dueDateStart, dueDateEnd);
    }
    public List<Task> getTasksByCreationDate(Date creationDateStart, Date creationDateEnd) {
        return taskRepository.findByCreationDateBetween(creationDateStart, creationDateEnd);
    }
    public List<Task> getTasksByLastUpdateDate(Date lastUpdateDateStart, Date lastUpdateDateEnd) {
        return taskRepository.findByLastUpdateDateBetween(lastUpdateDateStart, lastUpdateDateEnd);
    }
    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }
    public List<Task> getTasksByPriority(String priority) {
        return taskRepository.findByPriority(priority);
    }

    public List<Task> searchTasks(String title, String description, Date dueDate, String assignee, String category, String status, String priority, Date createdDate, Date lastUpdatedDate) {
        return taskRepository.findByTitleContainingAndDescriptionContainingAndDueDateAndAssigneeContainingAndCategoryContainingAndStatusContainingAndPriorityContainingAndCreationDateAndLastUpdateDate(
                title, description, dueDate, assignee, category, status, priority, createdDate, lastUpdatedDate);
    }
}
