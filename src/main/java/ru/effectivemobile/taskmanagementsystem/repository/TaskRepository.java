package ru.effectivemobile.taskmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.effectivemobile.taskmanagementsystem.entity.Task;
import ru.effectivemobile.taskmanagementsystem.entity.Task_;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @EntityGraph(Task_.GRAPH_TASK_FULL_GRAPH_)
    Page<Task> findAll(Specification<Task> spec, Pageable pageable);

    @Override
    @EntityGraph(Task_.GRAPH_TASK_FULL_GRAPH_)
    Optional<Task> findById(Long id);
}
