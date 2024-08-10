package ru.effectivemobile.taskmanagementsystem.model;

public record TaskSearchSettings(
        long accountId,
        TaskType taskType,
        int pageSize,
        int pageNumber
) {
    public enum TaskType {CREATED, ASSIGNED, ALL}
}
