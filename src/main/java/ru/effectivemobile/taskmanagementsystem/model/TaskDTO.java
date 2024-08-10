package ru.effectivemobile.taskmanagementsystem.model;

import ru.effectivemobile.taskmanagementsystem.entity.Task;

public record TaskDTO(
        Long id,
        String headline,
        String description,
        Task.Priority priority,
        Task.Status status,
        String authorEmail,
        String executorEmail
) {
    public TaskDTO(Task task) {
        this(
                task.getId(),
                task.getHeadline(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getAuthor().getEmail(),
                task.getExecutor().getEmail()
                );
    }
}
