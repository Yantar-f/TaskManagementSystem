package ru.effectivemobile.taskmanagementsystem.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.Optional;

public record TaskSearchSettingsDTO(
        long accountId,

        TaskSearchSettings.TaskType taskType,

        @Min(1)
        @Max(10)
        int pageSize,
        Optional<Integer> pageNumber
) {
}
