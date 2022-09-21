package com.yandex.practicum;

import com.yandex.practicum.managers.FileBackedTasksManager;
import com.yandex.practicum.tasks.*;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        File file = new File("data.csv");
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);

        Task task2 = new Task(TypeTask.TASK, "Задача-2", "Тестовая задача №2", Status.NEW);
        fileBackedTasksManager.addTask(task2);

        Task task3 = new Task(TypeTask.TASK, "Задача-3", "Тестовая задача №3", Status.NEW);
        fileBackedTasksManager.addTask(task3);

        Epic epic3 = new Epic(TypeTask.EPIC, "Эпик-3", "Тестовый эпик №3", Status.NEW);
        fileBackedTasksManager.addEpic(epic3);

        Subtask subtask3 = new Subtask(TypeTask.SUBTASK, "подзадача-3", "тестовая подзадача №3 к эпику-3",
                Status.NEW, epic3.getId());
        fileBackedTasksManager.addSubtask(subtask3);

        fileBackedTasksManager.getTaskById(task3.getId());
        fileBackedTasksManager.getTaskById(task2.getId());
        fileBackedTasksManager.getEpicById(epic3.getId());
        fileBackedTasksManager.getSubtaskById(subtask3.getId());

        fileBackedTasksManager.save();

        file = new File("save.csv");

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);

        fileBackedTasksManager2.save();

        System.out.println("Выходим, приехали!");

    }
}
