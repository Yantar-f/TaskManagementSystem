package ru.effectivemobile.taskmanagementsystem.config;

public abstract class TokenConfig {
    private final String issuer;
    private final long activationMs;
    private final long expirationMs;
    private final long allowedClockSkewS;
    private final String cookieName;
    private final String cookiePath;
    private final long cookieExpirationS;
    private final String secretKey;

    protected TokenConfig(String issuer,
                          long activationMs,
                          long expirationMs,
                          long allowedClockSkewS,
                          String cookieName,
                          String cookiePath,
                          long cookieExpirationS,
                          String secretKey) {
        this.issuer = issuer;
        this.activationMs = activationMs;
        this.expirationMs = expirationMs;
        this.allowedClockSkewS = allowedClockSkewS;
        this.cookieName = cookieName;
        this.cookiePath = cookiePath;
        this.cookieExpirationS = cookieExpirationS;
        this.secretKey = secretKey;
    }

    public String getIssuer() {
        return issuer;
    }

    public long getActivationMs() {
        return activationMs;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public long getAllowedClockSkewS() {
        return allowedClockSkewS;
    }

    public String getCookieName() {
        return cookieName;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public long getCookieExpirationS() {
        return cookieExpirationS;
    }

    public abstract String getTokenTypeName();

    public String getSecretKey() {
        return secretKey;
    }
}
