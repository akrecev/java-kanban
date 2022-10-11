package com.yandex.practicum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {

    protected String title; // краткое название задачи
    protected String description; // описание задачи
    protected int id; // id задачи
    protected Status status; // статус - "NEW", "IN_PROGRESS", "DONE"
    protected TypeTask typeTask; // тип задачи
    protected long duration;
    protected LocalDateTime startTime;

    public Task(TypeTask typeTask, String title, String description, int id, Status status) {
        this.typeTask = typeTask;
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }
    public Task(TypeTask typeTask, String title, String description, Status status) {
        this.typeTask = typeTask;
        this.title = title;
        this.description = description;
        this.status = status;
    }
    public Task(TypeTask typeTask, String title, String description, Status status, long duration,
                LocalDateTime startTime) {
        this.typeTask = typeTask;
        this.title = title;
        this.description = description;
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

    // задать название задачи
    public void setTitle(String title) {
        this.title = title;
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
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", typeTask=" + typeTask +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

}
