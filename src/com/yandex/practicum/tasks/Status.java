package com.yandex.practicum.tasks;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE;

    public static Status getStatus(String status) {
        switch (status) {
            case "NEW":
                return Status.NEW;
            case "IN_PROGRESS":
                return Status.IN_PROGRESS;
            case "DONE":
                return Status.DONE;
            default:
                return null;
        }
    }
}
