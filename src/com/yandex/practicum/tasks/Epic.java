package com.yandex.practicum.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    protected List<Integer> subTasksIds;

    protected LocalDateTime endTime;

    public Epic(TypeTask typeTask, String title, String description, int id, Status status) {
        super(typeTask, title, description, id, status);
        this.subTasksIds = new ArrayList<>();
    }

    public Epic(TypeTask typeTask, String title, String description, Status status) {
        super(typeTask, title, description, status);
        this.subTasksIds = new ArrayList<>();
    }

    public Epic(TypeTask typeTask, String title, String description, Status status, long duration,
                LocalDateTime startTime) {
        super(typeTask, title, description, status, duration, startTime);
        this.subTasksIds = new ArrayList<>();
    }

    public List<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void setSubTasksIds(List<Integer> subTasksIds) {
        this.subTasksIds = subTasksIds;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        if (subTasksIds != null) {
            return "Epic{" +
                    "subTasksIds=" + subTasksIds +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", id=" + id +
                    ", status='" + super.getStatus() + '\'' +
                    ", endTime=" + endTime +
                    ", duration=" + duration +
                    ", startTime=" + startTime +
                    '}';
        } else {
            return "Epic{" +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", id=" + id +
                    ", status='" + super.getStatus() + '\'' +
                    '}';
        }
    }
}
