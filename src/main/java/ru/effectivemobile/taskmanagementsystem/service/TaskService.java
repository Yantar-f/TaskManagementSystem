package ru.effectivemobile.taskmanagementsystem.service;

import org.springframework.data.domain.Slice;
import ru.effectivemobile.taskmanagementsystem.entity.Task;
import ru.effectivemobile.taskmanagementsystem.model.TaskCreationData;
import ru.effectivemobile.taskmanagementsystem.model.TaskSearchSettings;
import ru.effectivemobile.taskmanagementsystem.model.TaskUpdatingData;

public interface TaskService {
    Task createTask (TaskCreationData creationData);
    void updateTask (TaskUpdatingData updatingData);
    void deleteTask (Long taskId, Long actorId);

    Task        getTaskById  (Long taskId);
    Slice<Task> getTasksWith (TaskSearchSettings searchSettings);
}