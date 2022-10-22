package com.yandex.practicum.tasks;

import com.yandex.practicum.adapters.LocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    protected String title; // краткое название задачи
    protected String description; // описание задачи
    protected int id; // id задачи
    protected Status status; // статус - "NEW", "IN_PROGRESS", "DONE"
    protected TypeTask typeTask; // тип задачи
    protected long duration;
    protected LocalDateTime startTime;

    public Task(TypeTask typeTask, String title, String description, Status status) {
        this(typeTask, title, description, 1, status, 0L, LocalDateTime.MAX);
    }
    public Task(TypeTask typeTask, String title, String description, int id, Status status) {
        this(typeTask, title, description, id, status, 0L, LocalDateTime.MAX);
    }
    public Task(TypeTask typeTask, String title, String description, Status status, long duration,
                LocalDateTime startTime) {
        this(typeTask, title, description, 1, status, duration, startTime);
    }
    public Task(TypeTask typeTask, String title, String description, int id, Status status, long duration,
                LocalDateTime startTime) {
        this.typeTask = typeTask;
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    // получить тип задачи
    public TypeTask getTypeTask() {
        return typeTask;
    }

    // получить название задачи
    public String getTitle() {
        return title;
    }

    // получить описание задачи
    public String getDescription() {
        return description;
    }

    // задать описание задачи
    public void setDescription(String description) {
        this.description = description;
    }

    // получить id задачи
    public int getId() {
        return id;
    }

    // установить id задачи
    public void setId(int id) {
        this.id = id;
    }

    // получить статус задачи
    public Status getStatus() {
        return status;
    }

    // задать статус задачи
    public void setStatus(Status status) {
        this.status = status;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && title.equals(task.title)
                && description.equals(task.description) && status == task.status && typeTask == task.typeTask
                && startTime.equals(task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status, typeTask, duration, startTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "title=\"" + title + '\"' +
                ", description=\"" + description + '\"' +
                ", id=" + id +
                ", status=" + status +
                ", typeTask=" + typeTask +
                ", duration=" + duration +
                ", startTime=" + startTime.format(LocalDateTimeAdapter.formatter) +
                '}';
    }

}
