package ru.effectivemobile.taskmanagementsystem.util;

import ru.effectivemobile.taskmanagementsystem.model.TokenBody;

public interface TokenUtil {
    String      generateAccessToken (TokenBody body);
    TokenBody   parseAccessToken    (String token);
}
