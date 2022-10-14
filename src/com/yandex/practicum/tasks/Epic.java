package com.yandex.practicum.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public List<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return subTasksIds.equals(epic.subTasksIds) && endTime.equals(epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksIds, endTime);
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
