package ru.effectivemobile.taskmanagementsystem.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import ru.effectivemobile.taskmanagementsystem.entity.Task;

import java.util.List;

public class TaskSlice extends SliceImpl<Task> {
    public TaskSlice(List<Task> content, Pageable pageable, boolean hasNext) {
        super(content, pageable, hasNext);
    }

    public TaskSlice(List<Task> content) {
        super(content);
    }
}
