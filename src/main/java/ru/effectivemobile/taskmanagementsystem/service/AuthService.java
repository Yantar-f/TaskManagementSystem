package ru.effectivemobile.taskmanagementsystem.service;

import ru.effectivemobile.taskmanagementsystem.model.AccountCredentials;
import ru.effectivemobile.taskmanagementsystem.model.Session;

public interface AuthService {
    Session createSession(AccountCredentials credentials);
}
