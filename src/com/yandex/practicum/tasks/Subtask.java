package com.yandex.practicum.tasks;

import com.yandex.practicum.adapters.LocalDateTimeAdapter;

import java.time.LocalDateTime;

public class Subtask extends Task {

    protected int epicId;

    public Subtask(TypeTask typeTask, String title, String description, int id, Status status, int epicId, long duration,
                   LocalDateTime startTime) {
        super(typeTask, title, description, id, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(TypeTask typeTask, String title, String description, Status status, int epicId, long duration,
                   LocalDateTime startTime) {
        this(typeTask, title, description, 1, status, epicId, duration, startTime);
    }

    public Subtask(TypeTask typeTask, String title, String description, int id, Status status, int epicId) {
        this(typeTask, title, description, id, status, epicId, 0L, LocalDateTime.MAX);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "title=\"" + title + '\"' +
                ", description=\"" + description + '\"' +
                ", id=" + id +
                ", status=" + status +
                ", typeTask=" + typeTask +
                ", epicId=" + epicId +
                ", duration=" + duration +
                ", startTime=" + startTime.format(LocalDateTimeAdapter.formatter) +
                '}';
    }
}
