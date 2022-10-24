package com.yandex.practicum.tasks;

import com.yandex.practicum.adapters.LocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    protected List<Integer> subTasksIds;

    protected LocalDateTime endTime;

    public Epic(TypeTask typeTask, String title, String description, int id, Status status, List<Integer> subTasksIds) {
        super(typeTask, title, description, id, status);
        this.subTasksIds = subTasksIds;
        this.endTime = super.getEndTime();
    }

    public Epic(TypeTask typeTask, String title, String description, int id, Status status) {
        this(typeTask, title, description, id, status, new ArrayList<>());
    }

    public Epic(TypeTask typeTask, String title, String description, Status status) {
        this(typeTask, title, description, 1, status, new ArrayList<>());
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
    public String toString() {
        if (!subTasksIds.isEmpty()) {
            return "Epic{" +
                    "title=\"" + title + '"' +
                    ", description=\"" + description + '\"' +
                    ", id=" + id +
                    ", status=\"" + super.getStatus() + '\"' +
                    ", subTasksIds=" + subTasksIds +
                    ", duration=" + duration +
                    ", startTime=" + startTime.format(LocalDateTimeAdapter.formatter) +
                    ", endTime=" + endTime.format(LocalDateTimeAdapter.formatter) +
                    '}';
        } else {
            return "Epic{" +
                    ", title=\"" + title + '"' +
                    ", description=\"" + description + '\"' +
                    ", id=" + id +
                    ", status=\"" + super.getStatus() + '\"' +
                    '}';
        }
    }
}
