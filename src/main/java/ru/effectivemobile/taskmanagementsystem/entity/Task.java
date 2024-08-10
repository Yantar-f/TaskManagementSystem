package ru.effectivemobile.taskmanagementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "tasks")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Task[full-graph]",
                attributeNodes = {
                        @NamedAttributeNode("author"),
                        @NamedAttributeNode("executor")
                })
})
public class Task {
    public enum Status {WAITING, IN_PROCESS, COMPLETED;}
    public enum Priority {HIGH, MEDIUM, LOW;}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "headline")
    private String headline;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account executor;

    public Task() {

    }

    public Task(String headline,
                String description,
                Status status,
                Priority priority,
                Account author,
                Account executor) {
        this.headline = headline;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.author = author;
        this.executor = executor;
    }

    public long getId() {
        return id;
    }

    public String getHeadline() {
        return headline;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public Account getAuthor() {
        return author;
    }

    public Account getExecutor() {
        return executor;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setAuthor(Account author) {
        this.author = author;
    }

    public void setExecutor(Account executor) {
        this.executor = executor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return  Objects.equals(id, task.id) &&
                Objects.equals(headline, task.headline) &&
                Objects.equals(description, task.description) &&
                status == task.status &&
                priority == task.priority &&
                Objects.equals(author, task.author) &&
                Objects.equals(executor, task.executor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, headline, description, status, priority, author, executor);
    }
}
