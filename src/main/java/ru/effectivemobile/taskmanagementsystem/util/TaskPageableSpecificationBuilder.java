package ru.effectivemobile.taskmanagementsystem.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.effectivemobile.taskmanagementsystem.entity.Task;
import ru.effectivemobile.taskmanagementsystem.model.TaskSearchSettings;

public interface TaskPageableSpecificationBuilder {
    record TaskPageableSpecification(Specification<Task> specification, Pageable pageable){}
    TaskPageableSpecification buildFrom(TaskSearchSettings searchSettings);
}
