package ru.effectivemobile.taskmanagementsystem.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("invalid token: " + token);
    }
}
