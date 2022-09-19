package com.yandex.practicum.tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    protected List<Integer> subTasksIds;

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

    public void setSubTasksIds(List<Integer> subTasksIds) {
        this.subTasksIds = subTasksIds;
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
