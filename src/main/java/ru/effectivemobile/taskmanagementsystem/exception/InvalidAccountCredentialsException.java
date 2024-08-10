package ru.effectivemobile.taskmanagementsystem.exception;

public class InvalidAccountCredentialsException extends RuntimeException {
    public InvalidAccountCredentialsException() {
        super("Invalid account credentials");
    }
}
