package ru.effectivemobile.taskmanagementsystem.util.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.effectivemobile.taskmanagementsystem.entity.Account_;
import ru.effectivemobile.taskmanagementsystem.entity.Task;
import ru.effectivemobile.taskmanagementsystem.entity.Task_;
import ru.effectivemobile.taskmanagementsystem.model.TaskSearchSettings;
import ru.effectivemobile.taskmanagementsystem.util.TaskPageableSpecificationBuilder;

@Component
public class TaskPageableSpecificationBuilderImpl implements TaskPageableSpecificationBuilder {
    public TaskPageableSpecification buildFrom(TaskSearchSettings searchSettings) {
        var spec = buildSpecificationFrom(searchSettings);
        var page = buildPageableFrom(searchSettings);

        return new TaskPageableSpecification(spec, page);
    }

    private Pageable buildPageableFrom(TaskSearchSettings searchSettings) {
            return PageRequest.of(searchSettings.pageNumber(), searchSettings.pageSize());
    }

    private Specification<Task> buildSpecificationFrom(TaskSearchSettings settings) {
        switch (settings.taskType()) {
            default -> {
                return executorIdEquals(settings.accountId())
                        .or(authorIdEquals(settings.accountId()));
            }
            case CREATED -> {
                return authorIdEquals(settings.accountId());
            }
            case ASSIGNED -> {
                return executorIdEquals(settings.accountId());
            }
        }
    }

    private Specification<Task> authorIdEquals(long id) {
        return (root, query, builder) -> builder
                .equal(root.join(Task_.author).get(Account_.id), id);
    }

    private Specification<Task> executorIdEquals(long id) {
        return (root, query, builder) -> builder
                .equal(root.join(Task_.executor).get(Account_.id), id);
    }
}

