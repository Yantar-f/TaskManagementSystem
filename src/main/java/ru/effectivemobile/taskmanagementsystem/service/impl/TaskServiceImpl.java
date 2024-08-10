package ru.effectivemobile.taskmanagementsystem.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskmanagementsystem.entity.Account;
import ru.effectivemobile.taskmanagementsystem.entity.Task;
import ru.effectivemobile.taskmanagementsystem.exception.AccountNotFoundException;
import ru.effectivemobile.taskmanagementsystem.exception.NotAllowedTaskUpdateOperation;
import ru.effectivemobile.taskmanagementsystem.exception.TaskNotFoundException;
import ru.effectivemobile.taskmanagementsystem.model.TaskCreationData;
import ru.effectivemobile.taskmanagementsystem.model.TaskSearchSettings;
import ru.effectivemobile.taskmanagementsystem.model.TaskUpdatingData;
import ru.effectivemobile.taskmanagementsystem.repository.AccountRepository;
import ru.effectivemobile.taskmanagementsystem.repository.TaskRepository;
import ru.effectivemobile.taskmanagementsystem.service.TaskService;
import ru.effectivemobile.taskmanagementsystem.util.TaskPageableSpecificationBuilder;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final AccountRepository accountRepository;
    private final TaskPageableSpecificationBuilder taskPageableSpecificationBuilder;

    public TaskServiceImpl(TaskRepository taskRepository,
                           AccountRepository accountRepository,
                           TaskPageableSpecificationBuilder taskPageableSpecificationBuilder) {

        this.taskRepository = taskRepository;
        this.accountRepository = accountRepository;
        this.taskPageableSpecificationBuilder = taskPageableSpecificationBuilder;
    }

    @Override
    public Task createTask(TaskCreationData creationData) {
        Task newTask = buildTaskFrom(creationData);
        return taskRepository.save(newTask);
    }

    @Override
    @Transactional
    public void updateTask(TaskUpdatingData updatingData) {
        Task taskToUpdate = taskRepository
                .findById(updatingData.taskId())
                .orElseThrow(() -> new TaskNotFoundException(updatingData.taskId()));

        if (updatingData.actorId() != taskToUpdate.getAuthor().getId() &&
            updatingData.actorId() != taskToUpdate.getExecutor().getId()) {

            throw new NotAllowedTaskUpdateOperation();
        }

        if ( (  updatingData.headline().isPresent() ||
                updatingData.description().isPresent() ||
                updatingData.priority().isPresent() ||
                updatingData.executorId().isPresent() ) &&
                updatingData.actorId() != taskToUpdate.getAuthor().getId()) {

            throw new NotAllowedTaskUpdateOperation();
        }

        updatingData.executorId().ifPresent(executorId -> {
            Account newExecutor = accountRepository
                    .findById(executorId)
                    .orElseThrow(() -> new AccountNotFoundException(executorId));

            taskToUpdate.setExecutor(newExecutor);
        });

        updatingData.headline().ifPresent(taskToUpdate::setHeadline);
        updatingData.description().ifPresent(taskToUpdate::setDescription);
        updatingData.priority().ifPresent(taskToUpdate::setPriority);
        updatingData.status().ifPresent(taskToUpdate::setStatus);

        taskRepository.save(taskToUpdate);
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId, Long actorId) {
        taskRepository
                .findById(taskId)
                .ifPresent(task -> {
                    if (task.getAuthor().getId() != actorId)
                        throw new NotAllowedTaskUpdateOperation();

                    taskRepository.delete(task);
                });
    }

    @Override
    public Task getTaskById(Long taskId) {
        return taskRepository
                .findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    @Override
    public Slice<Task> getTasksWith(TaskSearchSettings searchSettings) {
        var pageableSpecification = buildPageableSpecificationFrom(searchSettings);

        Slice<Task> taskSlice = taskRepository
                .findAll(pageableSpecification.specification(), pageableSpecification.pageable());

        if (searchSettings.pageNumber() == 0 && taskSlice.isEmpty())
            throw new TaskNotFoundException(searchSettings);

        return taskSlice;
    }

    private Task buildTaskFrom(TaskCreationData creationData) {
        Account author = accountRepository
                .findById(creationData.actorId())
                .orElseThrow(() -> new AccountNotFoundException(creationData.actorId()));

        Account executor = accountRepository
                .findById(creationData.executorId())
                .orElseThrow(() -> new AccountNotFoundException(creationData.executorId()));

        return new Task(
                creationData.headline(),
                creationData.description(),
                creationData.status(),
                creationData.priority(),
                author,
                executor
        );
    }

    private TaskPageableSpecificationBuilder.TaskPageableSpecification buildPageableSpecificationFrom(
            TaskSearchSettings settings) {

        return taskPageableSpecificationBuilder.buildFrom(settings);
    }
}
