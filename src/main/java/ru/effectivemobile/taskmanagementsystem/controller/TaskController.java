package ru.effectivemobile.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Slice;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskmanagementsystem.entity.Task;
import ru.effectivemobile.taskmanagementsystem.model.TaskCreationData;
import ru.effectivemobile.taskmanagementsystem.model.TaskCreationDataDTO;
import ru.effectivemobile.taskmanagementsystem.model.TaskDTO;
import ru.effectivemobile.taskmanagementsystem.model.TaskSearchSettings;
import ru.effectivemobile.taskmanagementsystem.model.TaskSearchSettingsDTO;
import ru.effectivemobile.taskmanagementsystem.model.TaskSlice;
import ru.effectivemobile.taskmanagementsystem.model.TaskUpdatingData;
import ru.effectivemobile.taskmanagementsystem.model.TaskUpdatingDataDTO;
import ru.effectivemobile.taskmanagementsystem.service.TaskService;

import java.net.URI;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@Validated
@RestController
@Tag(name = "Task")
@RequestMapping(TaskController.TASKS_PATH)
public class TaskController {
    public static final String TASKS_PATH = "/tasks";
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(
            summary = "create task",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "task created",
                            headers = @Header(
                                    name = LOCATION,
                                    description = "task uri")),
                    @ApiResponse(
                            responseCode = "400",
                            description = "invalid data (constraint violations)",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)))})

    public ResponseEntity<Void> createTask(@Valid @RequestBody TaskCreationDataDTO creationDataDTO) {
        TaskCreationData creationData = buildTaskCreationDataFrom(creationDataDTO);
        Task newTask = taskService.createTask(creationData);
        URI taskUri = generateUriFor(newTask);

        return created(taskUri).build();
    }

    @GetMapping(
            path = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    @Operation(
            summary = "get task by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "task received",
                            content = @Content(schema = @Schema(implementation = TaskDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "invalid data (constraint violations)",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "task not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))})

    public ResponseEntity<TaskDTO> getTaskById(@PathVariable("id") Long taskId) {
        Task task = taskService.getTaskById(taskId);
        TaskDTO taskDto = new TaskDTO(task);
        return ok(taskDto);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(
            summary = "get tasks using search settings",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "page with tasks received",
                            content = @Content(schema = @Schema(implementation = TaskSlice.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "invalid data (constraint violations)",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "task not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))})

    public ResponseEntity<Slice<TaskDTO>> getTasks(@Valid TaskSearchSettingsDTO searchSettingsDTO) {
        TaskSearchSettings searchSettings = buildSearchSettingsFrom(searchSettingsDTO);
        Slice<Task> tasksSlice = taskService.getTasksWith(searchSettings);
        Slice<TaskDTO> dtoSlice = tasksSlice.map(TaskDTO::new);

        return ok(dtoSlice);
    }

    @PatchMapping
    @Operation(
            summary = "update task",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "task updated"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "invalid data (constraint violations)",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "not available changes",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "task not found",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)))})

    public ResponseEntity<Void> updateTask(@Valid @RequestBody TaskUpdatingDataDTO updatingDataDTO) {
        TaskUpdatingData updatingData = buildTaskUpdatingDataFrom(updatingDataDTO);

        taskService.updateTask(updatingData);

        return noContent().build();
    }

    @DeleteMapping
    @Operation(
            summary = "delete task",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "task deleted"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "invalid data (constraint violations)",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "not available changes",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)))})

    public ResponseEntity<Void> deleteTask(@RequestParam("task-id") Long taskId) {
        Long actorId = getCurrentActorId();

        taskService.deleteTask(taskId, actorId);

        return noContent().build();
    }

    private URI generateUriFor(Task newTask) {
        return URI.create(TASKS_PATH + "/" + newTask.getId());
    }

    private TaskSearchSettings buildSearchSettingsFrom(TaskSearchSettingsDTO dto) {
        return new TaskSearchSettings(
                dto.accountId(),
                dto.taskType(),
                dto.pageSize(),
                dto.pageNumber().orElse(0)
        );
    }

    private TaskCreationData buildTaskCreationDataFrom(TaskCreationDataDTO dto) {
        return new TaskCreationData(
                getCurrentActorId(),
                dto.headline(),
                dto.description(),
                dto.priority(),
                dto.status(),
                dto.executorId()
        );
    }

    private TaskUpdatingData buildTaskUpdatingDataFrom(TaskUpdatingDataDTO dto) {
        return new TaskUpdatingData(
                getCurrentActorId(),
                dto.taskId(),
                dto.headline(),
                dto.description(),
                dto.priority(),
                dto.status(),
                dto.executorId()
        );
    }

    private Long getCurrentActorId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
