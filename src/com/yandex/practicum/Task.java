package com.yandex.practicum;

public class Task {

    protected String title; // краткое название задачи
    protected String description; // описание задачи
    protected int id; // id задачи
    protected Status status; // статус - "NEW", "IN_PROGRESS", "DONE"

    public Task(String title, String description, int id, Status status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
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

    @Override
    public String toString() {
        return "Task{" +
//                "title='" + title + '\'' +
//                ", description='" + description + '\'' +
                ", id=" + id +
//                ", status='" + status + '\'' +
                '}';
    }
}
