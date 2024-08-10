package ru.effectivemobile.taskmanagementsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.effectivemobile.taskmanagementsystem.filter.AccessTokenFilter;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static ru.effectivemobile.taskmanagementsystem.controller.AccountController.ACCOUNTS_PATH;
import static ru.effectivemobile.taskmanagementsystem.controller.AuthController.AUTH_PATH;
import static ru.effectivemobile.taskmanagementsystem.controller.AuthController.SESSIONS_PATH;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String apiDocsPath;
    private final AccessTokenFilter accessTokenFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(@Value("${springdoc.api-docs.path}") String apiDocsPath,
                          AccessTokenFilter accessTokenFilter,
                          AccessDeniedHandler accessDeniedHandler,
                          AuthenticationEntryPoint authenticationEntryPoint) {

        this.apiDocsPath = apiDocsPath;
        this.accessTokenFilter = accessTokenFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .sessionManagement(ses -> ses.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(POST, ACCOUNTS_PATH, AUTH_PATH+SESSIONS_PATH).permitAll()
                        .requestMatchers(DELETE,AUTH_PATH+SESSIONS_PATH).permitAll()
                        .requestMatchers(GET, apiDocsPath + "/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exHandle -> exHandle
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(accessTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
