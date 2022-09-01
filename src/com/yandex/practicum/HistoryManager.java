package com.yandex.practicum;

import java.util.List;

public interface HistoryManager {

    // добавление просмотренной задачи в историю
    void add(Task task);

    // удаление задачи из истории просмотра
    void remove(int id);

    // получение истории просмотров задач
    List<Task> getHistory();

}
