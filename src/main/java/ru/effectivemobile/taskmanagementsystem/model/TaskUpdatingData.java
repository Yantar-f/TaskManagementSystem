package ru.effectivemobile.taskmanagementsystem.model;

import ru.effectivemobile.taskmanagementsystem.entity.Task;

import java.util.Optional;

public record TaskUpdatingData(
        Long actorId,
        Long taskId,
        Optional<String> headline,
        Optional<String> description,
        Optional<Task.Priority> priority,
        Optional<Task.Status> status,
        Optional<Long> executorId
) {
}
