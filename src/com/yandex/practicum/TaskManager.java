package com.yandex.practicum;

import java.util.List;

public interface TaskManager {

    /* ------ Методы для задач типа com.yandex.practicum.Task ------ */

    // получение списка всех задач
    List<Task> getTaskList();

    // удаление всех задач
    void delAllTasks();

    // получение задачи по id
    Task getTaskById(int id);

    // создание задачи
    int addTask(Task task);

    // обновление задачи
    int updateTask(Task task);

    // удаление задачи по идентификатору
    void deleteTaskById(int id);

    /* ------ Методы для задач типа com.yandex.practicum.Epic ------ */

    // обновление статуса эпика
    Status epicStatus(Epic epic);

    // получение списка эпиков
    List<Epic> getEpicList();

    // удаление всех эпиков
    void delAllEpics();

    // получение эпика по id
    Epic getEpicById(int id);

    // создание эпика
    int addEpic(Epic epic);

    // обновление эпика
    int updateEpic(Epic epic);

    // удаление эпика по идентификатору
    void deleteEpicById(int id);

    /* ------ Методы для подзадач типа com.yandex.practicum.Subtask ------ */

    // Получение списка всех подзадач определенного эпика
    List<Subtask> getSubtaskListByEpic(int id);

    // получение списка всех подзадач
    List<Subtask> getSubtaskList();

    // удаление всех подзадач
    void delAllSubtask();

    // получение подзадачи по id
    Subtask getSubtaskById(int id);

    // создание подзадачи
    int addSubtask(Subtask subtask);

    // обновление подзадачи
    int updateSubtask(Subtask subtask);

    // удаление подзадачи по идентификатору
    void deleteSubtaskById(int id);

    // получение истории просмотров задач
    List<Task> getHistory();
}