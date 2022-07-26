package com.yandex.practicum;

import com.yandex.practicum.managers.HttpTaskManager;
import com.yandex.practicum.managers.Managers;
import com.yandex.practicum.server.KVServer;
import com.yandex.practicum.tasks.*;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {

        KVServer kvServer = Managers.getDefaultKVServer();
        kvServer.start();
        HttpTaskManager taskManager = new HttpTaskManager(KVServer.PORT);
        Task task = new Task(TypeTask.TASK, "Task1", "Description Task1", Status.NEW, 10L,
                LocalDateTime.now());
        taskManager.addTask(task);
        Epic epic = new Epic(TypeTask.EPIC, "Epic1", "Description Epic1", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask(TypeTask.SUBTASK, "Subtask1", "Description Subtask1",
                Status.NEW, epic.getId(), 120L,
                task.getEndTime().plusMinutes(10L));
        taskManager.addSubtask(subtask);

        taskManager.getSubtaskById(subtask.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getTaskById(task.getId());

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());

        taskManager.setSave(false);

        taskManager.delAllTasks();
        taskManager.delAllEpics();
        taskManager.delAllSubtask();

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());

        taskManager.setSave(true);

        taskManager.load();

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());

        kvServer.stop();
    }
}
