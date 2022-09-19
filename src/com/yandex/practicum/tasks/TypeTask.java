package com.yandex.practicum.tasks;

public enum TypeTask {
    TASK,
    EPIC,
    SUBTASK;

    public static TypeTask getType(String type) {
        switch (type) {
            case "TASK":
                return TypeTask.TASK;
            case "EPIC":
                return TypeTask.EPIC;
            case "SUBTASK":
                return TypeTask.SUBTASK;
            default:
                return null;
        }
    }
}
