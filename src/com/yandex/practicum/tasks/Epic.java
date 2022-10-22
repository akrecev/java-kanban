package com.yandex.practicum.tasks;

import com.yandex.practicum.adapters.LocalDateTimeAdapter;

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
        if (subTasksIds != null) {
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
