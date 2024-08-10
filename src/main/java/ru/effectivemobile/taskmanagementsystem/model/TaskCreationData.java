package ru.effectivemobile.taskmanagementsystem.model;

import ru.effectivemobile.taskmanagementsystem.entity.Task;

public record TaskCreationData(
        Long actorId,
        String headline,
        String description,
        Task.Priority priority,
        Task.Status status,
        Long executorId
) {
}
