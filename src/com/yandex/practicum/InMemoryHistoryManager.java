package com.yandex.practicum;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new ArrayList<>();

    // добавление просмотренной задачи в историю
    @Override
    public void addInHistory(Task task) {
        history.add(task);
        if (history.size() > 10) {
            history.remove(0);
        }
    }

    // получение история просмотров задач
    @Override
    public List<Task> getHistory() {
        return history;
    }
}
