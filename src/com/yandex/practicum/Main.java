package com.yandex.practicum;

import com.yandex.practicum.managers.FileBackedTasksManager;
import com.yandex.practicum.tasks.*;

import java.io.File;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        System.out.println("-----------------------");

        File file = new File("data.csv");
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);

        Task task2 = new Task(TypeTask.TASK, "Задача-2", "Тестовая задача №2", Status.NEW, 500L,
                LocalDateTime.of(2022, 11, 1, 0, 0));
        fileBackedTasksManager.addTask(task2);

        Task task3 = new Task(TypeTask.TASK, "Задача-3", "Тестовая задача №3", Status.NEW, 500L,
                LocalDateTime.of(2022, 11, 20, 0, 0));
        fileBackedTasksManager.addTask(task3);

        Epic epic2 = new Epic(TypeTask.EPIC, "Эпик-3", "Тестовый эпик №3", Status.NEW);
        fileBackedTasksManager.addEpic(epic2);

        Subtask subtask2 = new Subtask(TypeTask.SUBTASK, "подзадача-3", "тестовая подзадача №3 к эпику-3",
                Status.NEW, epic2.getId(), 500L,
                LocalDateTime.of(2022, 11, 2, 0, 0));
        fileBackedTasksManager.addSubtask(subtask2);

        System.out.println("Остановка - Задание списка задач.");

        fileBackedTasksManager.getTaskById(task3.getId());
        fileBackedTasksManager.getTaskById(task2.getId());
        fileBackedTasksManager.getEpicById(epic2.getId());
        fileBackedTasksManager.getSubtaskById(subtask2.getId());

        System.out.println("Следующая остановка - Действия над задачами для создания истории");

        fileBackedTasksManager.save();

        file = new File("save.csv");

        System.out.println("А теперь - Сохранение в файл: save.csv");

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);

        System.out.println("В этом месте - Создание второго экземпляра FileBackedTasksManager'а из файла save.csv");

        fileBackedTasksManager2.save();

        System.out.println("Ну и запись fileBackedTasksManager2 в файл save.csv");


        System.out.println("-----------------------");
        System.out.println("Выходим, приехали!");

    }
}
