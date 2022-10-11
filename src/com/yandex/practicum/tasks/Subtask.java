package com.yandex.practicum.tasks;

import java.time.LocalDateTime;

public class Subtask extends Task {

    protected int epicId;

    public Subtask(TypeTask typeTask, String title, String description, int id, Status status, int epicId) {
        super(typeTask, title, description, id, status);
        this.epicId = epicId;
    }
    public Subtask(TypeTask typeTask, String title, String description, Status status, int epicId) {
        super(typeTask, title, description, status);
        this.epicId = epicId;
    }
    public Subtask(TypeTask typeTask, String title, String description, Status status, int epicId, long duration, LocalDateTime startTime) {
        super(typeTask, title, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", typeTask=" + typeTask +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}
