package ru.effectivemobile.taskmanagementsystem.service.impl;

import org.junit.jupiter.api.Test;
import ru.effectivemobile.taskmanagementsystem.entity.Account;
import ru.effectivemobile.taskmanagementsystem.entity.Task;
import ru.effectivemobile.taskmanagementsystem.exception.AccountNotFoundException;
import ru.effectivemobile.taskmanagementsystem.exception.NotAllowedTaskUpdateOperation;
import ru.effectivemobile.taskmanagementsystem.exception.TaskNotFoundException;
import ru.effectivemobile.taskmanagementsystem.model.TaskCreationData;
import ru.effectivemobile.taskmanagementsystem.model.TaskUpdatingData;
import ru.effectivemobile.taskmanagementsystem.repository.AccountRepository;
import ru.effectivemobile.taskmanagementsystem.repository.TaskRepository;
import ru.effectivemobile.taskmanagementsystem.service.TaskService;
import ru.effectivemobile.taskmanagementsystem.util.TaskPageableSpecificationBuilder;

import java.util.Optional;

import static org.instancio.Instancio.create;
import static org.instancio.Instancio.of;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceImplTest {
    private final TaskPageableSpecificationBuilder pageableSpecificationBuilder =
            mock(TaskPageableSpecificationBuilder.class);

    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final TaskRepository taskRepository = mock(TaskRepository.class);

    private final TaskService sut =
            new TaskServiceImpl(taskRepository, accountRepository, pageableSpecificationBuilder);

    @Test
    public void Creating_task_is_successful() {
        var creationData = create(TaskCreationData.class);
        var expectedTask = create(Task.class);
        var expectedActor = create(Account.class);
        var expectedExecutor = create(Account.class);

        when(accountRepository.findById(eq(creationData.actorId())))
                .thenReturn(Optional.of(expectedActor));

        when(accountRepository.findById(eq(creationData.executorId())))
                .thenReturn(Optional.of(expectedExecutor));

        when(taskRepository.save(any()))
                .thenReturn(expectedTask);

        var actualTask = sut.createTask(creationData);

        assertEquals(expectedTask, actualTask);

        verify(taskRepository)
                .save(argThat(task ->
                        task.getExecutor().equals(expectedExecutor) &&
                        task.getAuthor().equals(expectedActor) &&
                        task.getStatus().equals(creationData.status()) &&
                        task.getPriority().equals(creationData.priority()) &&
                        task.getHeadline().equals(creationData.headline()) &&
                        task.getDescription().equals(creationData.description())));
    }

    @Test
    public void Creating_task_with_not_existing_author_is_failed() {
        var creationData = create(TaskCreationData.class);
        var expectedExecutor = create(Account.class);

        when(accountRepository.findById(eq(creationData.actorId())))
                .thenReturn(Optional.empty());

        when(accountRepository.findById(eq(creationData.executorId())))
                .thenReturn(Optional.of(expectedExecutor));

        assertThrows(AccountNotFoundException.class, () -> sut.createTask(creationData));

        verify(taskRepository, never())
                .save(any());
    }

    @Test
    public void Creating_task_with_not_existing_executor_is_failed() {
        var creationData = create(TaskCreationData.class);
        var expectedActor = create(Account.class);

        when(accountRepository.findById(eq(creationData.executorId())))
                .thenReturn(Optional.empty());

        when(accountRepository.findById(eq(creationData.actorId())))
                .thenReturn(Optional.of(expectedActor));

        assertThrows(AccountNotFoundException.class, () -> sut.createTask(creationData));

        verify(taskRepository, never())
                .save(any());
    }

    @Test
    public void Updating_task_is_successful() {
        var expectedActor = create(Account.class);
        var expectedExecutor = create(Account.class);
        var expectedNewExecutor = create(Account.class);

        var updatingData = of(TaskUpdatingData.class)
                .set(field(TaskUpdatingData::actorId),expectedActor.getId())
                .set(field(TaskUpdatingData::executorId), Optional.of(expectedNewExecutor.getId()))
                .create();

        var expectedTask = of(Task.class)
                .set(field(Task::getId), updatingData.taskId())
                .set(field(Task::getAuthor), expectedActor)
                .set(field(Task::getExecutor), expectedExecutor)
                .create();

        var expectedUpdatedTask = of(Task.class)
                .set(field(Task::getId), updatingData.taskId())
                .set(field(Task::getHeadline), updatingData.headline().orElse(expectedTask.getHeadline()))
                .set(field(Task::getDescription), updatingData.description().orElse(expectedTask.getDescription()))
                .set(field(Task::getStatus), updatingData.status().orElse(expectedTask.getStatus()))
                .set(field(Task::getPriority), updatingData.priority().orElse(expectedTask.getPriority()))
                .set(field(Task::getExecutor),
                        updatingData.executorId().map(id -> expectedNewExecutor).orElse(expectedTask.getExecutor()))
                .create();

        when(taskRepository.findById(eq(updatingData.taskId())))
                .thenReturn(Optional.of(expectedTask));

        when(accountRepository.findById(expectedNewExecutor.getId()))
                .thenReturn(Optional.of(expectedNewExecutor));

        sut.updateTask(updatingData);

        verify(taskRepository)
                .save(argThat(task ->
                        updatingData.headline()
                                .map(expectedUpdatedTask.getHeadline()::equals)
                                .orElse(true) &&
                        updatingData.description()
                                .map(expectedUpdatedTask.getDescription()::equals)
                                .orElse(true) &&
                        updatingData.status()
                                .map(expectedUpdatedTask.getStatus()::equals)
                                .orElse(true) &&
                        updatingData.priority()
                                .map(expectedUpdatedTask.getPriority()::equals)
                                .orElse(true) &&
                        updatingData.executorId()
                                .map(id -> id == expectedUpdatedTask.getExecutor().getId())
                                .orElse(true)));
    }

    @Test
    public void Updating_task_without_permissions_for_executor_is_failed() {
        var expectedActor = create(Account.class);
        var expectedExecutor = create(Account.class);

        var updatingData = of(TaskUpdatingData.class)
                .set(field(TaskUpdatingData::actorId), expectedExecutor.getId())
                .create();

        var expectedTask = of(Task.class)
                .set(field(Task::getId), updatingData.taskId())
                .set(field(Task::getAuthor), expectedActor)
                .set(field(Task::getExecutor), expectedExecutor)
                .create();

        when(taskRepository.findById(eq(expectedTask.getId())))
                .thenReturn(Optional.of(expectedTask));

        assertThrows(NotAllowedTaskUpdateOperation.class, () -> sut.updateTask(updatingData));

        verify(taskRepository, never())
                .save(any());
    }

    @Test
    public void Updating_task_without_any_permission_is_failed() {
        var expectedActor = create(Account.class);
        var expectedExecutor = create(Account.class);

        var updatingData = of(TaskUpdatingData.class)
                .set(field(TaskUpdatingData::executorId), Optional.empty())
                .set(field(TaskUpdatingData::headline), Optional.empty())
                .set(field(TaskUpdatingData::description), Optional.empty())
                .set(field(TaskUpdatingData::status), Optional.empty())
                .set(field(TaskUpdatingData::priority), Optional.empty())
                .create();

        var expectedTask = of(Task.class)
                .set(field(Task::getId), updatingData.taskId())
                .set(field(Task::getAuthor), expectedActor)
                .set(field(Task::getExecutor), expectedExecutor)
                .create();

        when(taskRepository.findById(eq(expectedTask.getId())))
                .thenReturn(Optional.of(expectedTask));

        assertThrows(NotAllowedTaskUpdateOperation.class, () -> sut.updateTask(updatingData));

        verify(taskRepository, never())
                .save(any());
    }

    @Test
    public void Updating_task_with_not_existing_task_id_is_failed() {
        var updatingData = create(TaskUpdatingData.class);

        when(taskRepository.findById(eq(updatingData.taskId())))
                .thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> sut.updateTask(updatingData));

        verify(taskRepository, never())
                .save(any());
    }

    @Test
    public void Updating_task_with_not_existing_executor_id_is_failed() {
        var expectedAuthor = create(Account.class);
        var expectedExecutorId = create(Long.class);

        var expectedTask = of(Task.class)
                .set(field(Task::getAuthor), expectedAuthor)
                .create();

        var updatingData = of(TaskUpdatingData.class)
                .set(field(TaskUpdatingData::taskId), expectedTask.getId())
                .set(field(TaskUpdatingData::actorId), expectedAuthor.getId())
                .set(field(TaskUpdatingData::executorId), Optional.of(expectedExecutorId))
                .create();

        when(taskRepository.findById(eq(updatingData.taskId())))
                .thenReturn(Optional.of(expectedTask));

        when(accountRepository.findById(eq(expectedExecutorId)))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> sut.updateTask(updatingData));

        verify(taskRepository, never())
                .save(any());
    }

    @Test
    public void Deleting_task_is_successful() {
        var expectedTask = create(Task.class);

        when(taskRepository.findById(eq(expectedTask.getId())))
                .thenReturn(Optional.of(expectedTask));

        sut.deleteTask(expectedTask.getId(), expectedTask.getAuthor().getId());

        verify(taskRepository)
                .delete(eq(expectedTask));
    }

    @Test
    public void Deleting_task_without_permission_is_failed() {
        var expectedTask = create(Task.class);

        when(taskRepository.findById(eq(expectedTask.getId())))
                .thenReturn(Optional.of(expectedTask));

        assertThrows(NotAllowedTaskUpdateOperation.class,
                () -> sut.deleteTask(expectedTask.getId(), create(Long.class)));

        verify(taskRepository, never())
                .delete(any());
    }

    @Test
    public void Getting_task_by_id_is_successful() {
        var expectedTask = create(Task.class);

        when(taskRepository.findById(eq(expectedTask.getId())))
                .thenReturn(Optional.of(expectedTask));

        var actualTask = sut.getTaskById(expectedTask.getId());

        assertEquals(expectedTask, actualTask);

    }

    @Test
    public void Getting_task_by_not_existing_id_is_failed() {
        var taskId = create(Long.class);

        when(taskRepository.findById(eq(taskId)))
                .thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> sut.getTaskById(taskId));
    }
}