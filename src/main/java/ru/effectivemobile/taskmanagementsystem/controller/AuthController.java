package ru.effectivemobile.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskmanagementsystem.config.AccessTokenConfig;
import ru.effectivemobile.taskmanagementsystem.config.TokenConfig;
import ru.effectivemobile.taskmanagementsystem.model.AccountCredentials;
import ru.effectivemobile.taskmanagementsystem.model.Session;
import ru.effectivemobile.taskmanagementsystem.service.AuthService;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.effectivemobile.taskmanagementsystem.controller.AuthController.AUTH_PATH;
import static ru.effectivemobile.taskmanagementsystem.controller.AuthController.SESSIONS_PATH;

@Validated
@RestController
@Tag(name = "Auth")
@RequestMapping(AUTH_PATH+SESSIONS_PATH)
public class AuthController {
    public static final String AUTH_PATH = "/auth";
    public static final String SESSIONS_PATH = "/sessions";
    private final AuthService authService;
    private final AccessTokenConfig accessTokenConfig;

    public AuthController(AuthService authService,
                          AccessTokenConfig accessTokenConfig) {

        this.authService = authService;
        this.accessTokenConfig = accessTokenConfig;
    }

    @PostMapping
    @Operation(
            summary = "log in",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful logged in"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "invalid account credentials",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "account not found",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)))})

    public ResponseEntity<Void> createSession(@Valid @RequestBody AccountCredentials credentials) {
        Session session = authService.createSession(credentials);

        return ResponseEntity.ok()
                .header(SET_COOKIE, createAccessTokenCookie(session.accessToken()))
                .build();
    }

    @DeleteMapping
    @Operation(
            summary = "log out",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "successful logged out")})
    public ResponseEntity<Void> deleteSession() {
        return ResponseEntity.noContent()
                .header(SET_COOKIE, createAccessTokenCleaningCookie())
                .build();
    }

    private String createAccessTokenCookie(String accessToken) {
        return createTokenCookie(accessToken, accessTokenConfig);
    }

    private String createAccessTokenCleaningCookie() {
        return createCleaningCookie(accessTokenConfig.getCookieName(), accessTokenConfig.getCookiePath());
    }

    private String createTokenCookie(String token, TokenConfig config) {
        return ResponseCookie.from(config.getCookieName())
                .value(token)
                .path(config.getCookiePath())
                .maxAge(config.getCookieExpirationS())
                .httpOnly(true)
                .build()
                .toString();
    }

    private String createCleaningCookie(String name, String path) {
        return ResponseCookie.from(name)
                .path(path)
                .maxAge(0)
                .build()
                .toString();
    }
}
