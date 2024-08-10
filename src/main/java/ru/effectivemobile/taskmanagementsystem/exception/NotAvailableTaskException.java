package ru.effectivemobile.taskmanagementsystem.exception;

public class NotAvailableTaskException extends RuntimeException {
    public NotAvailableTaskException(Long taskId) {
        super("Task not available: id = " + taskId);
    }
}
