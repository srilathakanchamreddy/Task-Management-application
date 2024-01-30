package com.example.taskmanagement.Repositories;

import com.example.taskmanagement.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTitleContaining(String keyword);
    List<Task> findByDescriptionContaining(String keyword);

    List<Task> findByStatus(String status);

    List<Task> findByPriority(String priority);

    List<Task> findByAssignee(String assignee);
    List<Task> findByCategory(String category);
    List<Task> findByCreationDateBetween(Date startDate, Date endDate);
    List<Task> findByLastUpdateDateBetween(Date startDate, Date endDate);
    List<Task> findByDueDateBetween(Date startDate, Date endDate);


    //List<Task> searchTasks(String title, String description, Date dueDate, String assignee, String category, String status, String priority, Date createdDate, Date lastUpdatedDate);

    @Query("SELECT t FROM Task t WHERE " +
            "(:title IS NULL OR t.title LIKE %:title%) AND " +
            "(:description IS NULL OR t.description LIKE %:description%) AND " +
            "(:dueDate IS NULL OR t.dueDate = :dueDate) AND " +
            "(:assignee IS NULL OR t.assignee LIKE %:assignee%) AND " +
            "(:category IS NULL OR t.category LIKE %:category%) AND " +
            "(:status IS NULL OR t.status LIKE %:status%) AND " +
            "(:priority IS NULL OR t.priority LIKE %:priority%) AND " +
            "(:createdDate IS NULL OR t.creationDate = :createdDate) AND " +
            "(:lastUpdatedDate IS NULL OR t.lastUpdateDate = :lastUpdatedDate)")
    List<Task> findByTitleContainingAndDescriptionContainingAndDueDateAndAssigneeContainingAndCategoryContainingAndStatusContainingAndPriorityContainingAndCreationDateAndLastUpdateDate(
            @Param("title") String title, @Param("description") String description, @Param("dueDate") Date dueDate,
            @Param("assignee") String assignee, @Param("category") String category, @Param("status") String status,
            @Param("priority") String priority, @Param("createdDate") Date createdDate,
            @Param("lastUpdatedDate") Date lastUpdatedDate);




}
