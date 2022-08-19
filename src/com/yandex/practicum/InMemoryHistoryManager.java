package com.yandex.practicum;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final int HISTORY_SIZE = 10;
    private final List<Task> history = new ArrayList<>();

    // добавление просмотренной задачи в историю
    @Override
    public void addInHistory(Task task) {
        history.add(task);
        if (history.size() > HISTORY_SIZE) {
            history.remove(0);
        }
    }

    // получение истории просмотров задач
    @Override
    public List<Task> getHistory() {
        return history;
    }
}
