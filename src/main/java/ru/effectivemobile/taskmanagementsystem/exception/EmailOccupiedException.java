package ru.effectivemobile.taskmanagementsystem.exception;

public class EmailOccupiedException extends RuntimeException {
    public EmailOccupiedException(String email) {
        super("Email " + email + " already in use");
    }
}
