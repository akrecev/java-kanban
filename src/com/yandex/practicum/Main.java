package com.yandex.practicum;

import com.yandex.practicum.managers.Managers;
import com.yandex.practicum.managers.TaskManager;
import com.yandex.practicum.tasks.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();


        Task task1 = new Task(TypeTask.TASK,"Задача-1", "Тестовая задача №1", 0, Status.NEW);
        taskManager.addTask(task1);

        Task task2 = new Task(TypeTask.TASK, "Задача-2", "Тестовая задача №2", 0, Status.NEW);
        taskManager.addTask(task2);

        Epic epic1 = new Epic(TypeTask.EPIC, "Эпик-1", "Тестовый эпик №1", 0, Status.NEW);
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask(TypeTask.SUBTASK, "подзадача-1", "тестовая подзадача №1 к эпику-1", 0, Status.NEW, 3);
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask(TypeTask.SUBTASK, "подзадача-2", "тестовая подзадача №2 к эпику-1", 0, Status.NEW, 3);
        taskManager.addSubtask(subtask2);

        Subtask subtask3 = new Subtask(TypeTask.SUBTASK, "подзадача-3", "тестовая подзадача №3 к эпику-1", 0, Status.NEW, 3);
        taskManager.addSubtask(subtask3);

        Epic epic2 = new Epic(TypeTask.EPIC, "Эпик-2", "Тестовый эпик №2", 0, Status.NEW);
        taskManager.addEpic(epic2);

        System.out.println("Список задач: " + taskManager.getTaskList().toString());
        System.out.println("Список эпиков: " + taskManager.getEpicList().toString());
        System.out.println("Список подзадач: " + taskManager.getSubtaskList().toString());
        System.out.println();
        System.out.println("История просмотра задач: " + taskManager.getHistory());

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        System.out.println("История просмотра задач: " + taskManager.getHistory());
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        taskManager.getEpicById(7);
        System.out.println("История просмотра задач: " + taskManager.getHistory());
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        System.out.println("История просмотра задач: " + taskManager.getHistory());
        taskManager.getSubtaskById(5);
        taskManager.getEpicById(3);
        System.out.println("История просмотра задач: " + taskManager.getHistory());
        taskManager.deleteTaskById(1);
        System.out.println("История просмотра задач: " + taskManager.getHistory());
        taskManager.deleteEpicById(3);
        System.out.println("История просмотра задач: " + taskManager.getHistory());

    }
}
