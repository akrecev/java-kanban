package com.yandex.practicum;

import com.yandex.practicum.managers.HttpTaskManager;
import com.yandex.practicum.server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        /*TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);

        Task task = new Task(TypeTask.TASK, "Task1", "Description Task1", Status.NEW, 500L,
                LocalDateTime.of(2022, 11, 1, 0, 0));
        taskManager.addTask(task);
        Epic epic = new Epic(TypeTask.EPIC, "Epic1", "Description Epic1", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask(TypeTask.SUBTASK, "Subtask1", "Description Subtask1",
                Status.NEW, epic.getId(), 120L,
                LocalDateTime.of(2022, 10, 21, 20, 0));
        taskManager.addSubtask(subtask);
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask.getId());

        Task task2 = new Task(TypeTask.TASK, "Task2", "Description Task2", Status.IN_PROGRESS,
                500L, LocalDateTime.of(2023, 11, 1, 0, 0));
        taskManager.addTask(task2);
        Epic epic2 = new Epic(TypeTask.EPIC, "Epic2", "Description Epic2", Status.NEW);
        taskManager.addEpic(epic2);
        Subtask subtask2 = new Subtask(TypeTask.SUBTASK, "Subtask2", "Description Subtask2",
                Status.DONE, epic2.getId(), 120L,
                LocalDateTime.of(2023, 10, 21, 20, 0));
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask(TypeTask.SUBTASK, "Subtask3", "Description Subtask3",
                Status.DONE, epic2.getId(), 120L,
                LocalDateTime.of(2023, 1, 21, 20, 0));
        taskManager.addSubtask(subtask3);
        taskManager.getTaskById(task2.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getSubtaskById(subtask2.getId());

        httpTaskServer.start();*/

        HttpTaskManager taskManager = new HttpTaskManager(KVServer.PORT);
//        KVServer kvServer = Managers.getDefaultKVServer();
//        Task task = new Task(TypeTask.TASK, "Task1", "Description Task1", Status.NEW, 10L,
//                LocalDateTime.now());
//        taskManager.addTask(task);
//        Epic epic = new Epic(TypeTask.EPIC, "Epic1", "Description Epic1", Status.NEW);
//        taskManager.addEpic(epic);
//        Subtask subtask = new Subtask(TypeTask.SUBTASK, "Subtask1", "Description Subtask1",
//                Status.NEW, epic.getId(), 120L,
//                task.getEndTime().plusMinutes(10L));
//        taskManager.addSubtask(subtask);
//        kvServer.start();




    }
}
