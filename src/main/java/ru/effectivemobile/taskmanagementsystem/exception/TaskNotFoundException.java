package ru.effectivemobile.taskmanagementsystem.exception;

import ru.effectivemobile.taskmanagementsystem.model.TaskSearchSettings;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long taskId) {
        super("Task not found: id = " + taskId);
    }

    public TaskNotFoundException(TaskSearchSettings searchSettings) {
        super("Task with task type " + searchSettings.taskType().name() +
                " and account id " + searchSettings.accountId() + " not found");
    }
}
