package ru.effectivemobile.taskmanagementsystem.exception;

import ru.effectivemobile.taskmanagementsystem.config.TokenConfig;

public class MissingTokenException extends RuntimeException {
    public MissingTokenException(TokenConfig config) {
        super("Missing " + config.getTokenTypeName());
    }
}
