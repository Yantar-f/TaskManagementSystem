package ru.effectivemobile.taskmanagementsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenConfig extends TokenConfig {
    protected AccessTokenConfig(
            @Value("${task-management-system.app.security.access-token.issuer}") String issuer,
            @Value("${task-management-system.app.security.access-token.activation-ms}") long activationMs,
            @Value("${task-management-system.app.security.access-token.expiration-ms}") long expirationMs,
            @Value("${task-management-system.app.security.access-token.allowed-clock-skew-s}") long allowedClockSkew,
            @Value("${task-management-system.app.security.access-token.cookie-name}") String cookieName,
            @Value("${task-management-system.app.security.access-token.cookie-path}") String cookiePath,
            @Value("${task-management-system.app.security.access-token.cookie-expiration-s}") long cookieExpirationS,
            @Value("${task-management-system.app.security.access-token.secret-key}") String secretKey) {
        super(
                issuer,
                activationMs,
                expirationMs,
                allowedClockSkew,
                cookieName,
                cookiePath,
                cookieExpirationS,
                secretKey
        );
    }

    @Override
    public String getTokenTypeName() {
        return "access token";
    }
}
