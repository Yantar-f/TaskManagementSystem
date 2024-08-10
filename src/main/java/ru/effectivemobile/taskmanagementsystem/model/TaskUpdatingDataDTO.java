package ru.effectivemobile.taskmanagementsystem.model;

import jakarta.validation.constraints.NotNull;
import ru.effectivemobile.taskmanagementsystem.config.NotBlankOptionalString;
import ru.effectivemobile.taskmanagementsystem.entity.Task;

import java.util.Optional;

public record TaskUpdatingDataDTO(
        @NotNull
        Long taskId,

        @NotBlankOptionalString
        Optional<String> headline,

        @NotBlankOptionalString
        Optional<String> description,

        Optional<Task.Status> status,

        Optional<Task.Priority> priority,

        Optional<Long> executorId
) {
}
