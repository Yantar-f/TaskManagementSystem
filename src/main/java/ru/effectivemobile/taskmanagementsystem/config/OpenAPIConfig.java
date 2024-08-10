package ru.effectivemobile.taskmanagementsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Task Management API Documentation",
                version = "1.0"),
        servers = @Server(
                description = "Dev local server",
                url = "http://localhost:8080"))
public class OpenAPIConfig {}
