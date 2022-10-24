package com.yandex.practicum.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.practicum.client.KVTaskClient;
import com.yandex.practicum.tasks.Epic;
import com.yandex.practicum.tasks.Subtask;
import com.yandex.practicum.tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient client;
    private final Gson gson;
    private static final String TASKS_KEY = "/tasks";
    private static final String EPICS_KEY = "/epics";
    private static final String SUBTASKS_KEY = "/subtasks";
    private static final String HISTORY_KEY = "/history";

    public HttpTaskManager(int port) throws IOException {

        gson = Managers.getGson();
        client = new KVTaskClient(port);
    }

    public void save() {
        client.put(TASKS_KEY, gson.toJson(getTasks()));
        client.put(EPICS_KEY, gson.toJson(getEpics()));
        client.put(SUBTASKS_KEY, gson.toJson(getSubtasks()));
        client.put(HISTORY_KEY, gson.toJson(getHistoryIds()));
    }

    public void load() {
        Type tasksType = new TypeToken<Map<Integer, Task>>() {
        }.getType();
        Map<Integer, Task> tasks = gson.fromJson(client.load(TASKS_KEY), tasksType);
        tasks.forEach((k, v) -> {
            if (k > this.generateId) {
                this.generateId = k;
            }
            this.tasks.put(k, v);
            this.prioritizedTasks.add(v);
            this.generateId++;
        });

        Type epicsType = new TypeToken<Map<Integer, Epic>>() {
        }.getType();
        Map<Integer, Epic> epics = gson.fromJson(client.load(EPICS_KEY), epicsType);
        epics.forEach((k, v) -> {
            if (k > this.generateId) {
                this.generateId = k;
            }
            this.epics.put(k, v);
            this.updateEpicDurationAndStartEndTime(k);
            this.generateId++;
        });

        Type subtasksType = new TypeToken<Map<Integer, Subtask>>() {
        }.getType();
        Map<Integer, Subtask> subtasks = gson.fromJson(client.load(SUBTASKS_KEY), subtasksType);
        subtasks.forEach((k, v) -> {
            if (k > this.generateId) {
                this.generateId = k;
            }
            this.subtasks.put(k, v);
            this.prioritizedTasks.add(v);
            this.generateId++;
        });

        Type historyType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> history = gson.fromJson(client.load(HISTORY_KEY), historyType);
        for (Integer taskId : history) {
            this.historyManager.add(this.findTask(taskId));
        }
    }

}
