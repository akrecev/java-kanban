package com.yandex.practicum;

import java.util.List;

public interface HistoryManager {

    // добавление просмотренной задачи в историю
    void addInHistory(Task task);

    // получение истории просмотров задач
    List<Task> getHistory();

}
