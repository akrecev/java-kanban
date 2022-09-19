package com.yandex.practicum.managers;

import com.yandex.practicum.tasks.Task;

import java.util.List;

public interface HistoryManager {

    // добавление просмотренной задачи в историю
    void add(Task task);

    // удаление задачи из истории просмотра
    void remove(int id);

    // получение истории просмотров задач
    List<Task> getHistory();

}
