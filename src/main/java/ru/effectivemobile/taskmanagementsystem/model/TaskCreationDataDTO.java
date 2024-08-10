package ru.effectivemobile.taskmanagementsystem.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.effectivemobile.taskmanagementsystem.entity.Task;

public record TaskCreationDataDTO(
        @NotEmpty
        String headline,

        @NotEmpty
        String description,

        //@EnumType(enumClass = Task.Status.class)
        @NotNull
        Task.Status status,

        @NotNull
        //@EnumType(enumClass = Task.Priority.class)
        Task.Priority priority,

        @NotNull
        Long executorId
) {
}
