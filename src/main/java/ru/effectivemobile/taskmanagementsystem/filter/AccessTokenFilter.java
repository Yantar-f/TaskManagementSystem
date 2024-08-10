package ru.effectivemobile.taskmanagementsystem.filter ;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.effectivemobile.taskmanagementsystem.config.AccessTokenConfig;
import ru.effectivemobile.taskmanagementsystem.exception.InvalidTokenException;
import ru.effectivemobile.taskmanagementsystem.model.TokenBody;
import ru.effectivemobile.taskmanagementsystem.util.TokenUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class AccessTokenFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;
    private final AccessTokenConfig accessTokenConfig;

    public AccessTokenFilter(TokenUtil tokenUtil,
                             AccessTokenConfig accessTokenConfig) {

        this.tokenUtil = tokenUtil;
        this.accessTokenConfig = accessTokenConfig;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        extractAccessTokenFrom(request).ifPresent(token -> {
            try {
                TokenBody tokenBody = tokenUtil.parseAccessToken(token);

                UsernamePasswordAuthenticationToken contextAuthToken =
                        new UsernamePasswordAuthenticationToken(tokenBody.getSubjectId(), null, null);

                contextAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(contextAuthToken);
            } catch (InvalidTokenException ignored) {}
        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractAccessTokenFrom(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> cookie.getName().equals(accessTokenConfig.getCookieName()))
                .findFirst()
                .map(Cookie::getValue);
    }
}
