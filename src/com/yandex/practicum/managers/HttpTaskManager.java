package com.yandex.practicum.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.practicum.client.KVTaskClient;
import com.yandex.practicum.tasks.Epic;
import com.yandex.practicum.tasks.Subtask;
import com.yandex.practicum.tasks.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient client;
    private final Gson gson;
    private static final String TASKS_KEY = "/tasks";
    private static final String EPICS_KEY = "/epics";
    private static final String SUBTASKS_KEY = "/subtasks";
    private static final String HISTORY_KEY = "/history";
    private boolean isSave = true;

    public HttpTaskManager(int port) {

        gson = Managers.getGson();
        client = new KVTaskClient(port);
    }

    public void save() {
        if (isSave) {
            client.put(TASKS_KEY, gson.toJson(getTaskList()));
            client.put(EPICS_KEY, gson.toJson(getEpicList()));
            client.put(SUBTASKS_KEY, gson.toJson(getSubtaskList()));
            client.put(HISTORY_KEY, gson.toJson(getHistoryIds()));
        }
    }

    public void load() {
        Type tasksType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> tasks = gson.fromJson(client.load(TASKS_KEY), tasksType);
        tasks.forEach((task) -> {
            int id = task.getId();
            if (id > this.generateId) {
                this.generateId = id;
            }
            this.tasks.put(id, task);
            this.prioritizedTasks.add(task);
            this.generateId++;
        });

        Type epicsType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        ArrayList<Epic> epics = gson.fromJson(client.load(EPICS_KEY), epicsType);
        epics.forEach((epic) -> {
            int id = epic.getId();
            if (id > this.generateId) {
                this.generateId = id;
            }
            this.epics.put(id, epic);
            this.generateId++;
        });

        Type subtasksType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        ArrayList<Subtask> subtasks = gson.fromJson(client.load(SUBTASKS_KEY), subtasksType);
        subtasks.forEach((subtask) -> {
            int id = subtask.getId();
            if (id > this.generateId) {
                this.generateId = id;
            }
            this.subtasks.put(id, subtask);
            this.prioritizedTasks.add(subtask);
            this.generateId++;
        });

        Type historyType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> history = gson.fromJson(client.load(HISTORY_KEY), historyType);
        for (Integer taskId : history) {
            this.historyManager.add(this.findTask(taskId));
        }
    }

    public void setSave(boolean save) {
        isSave = save;
    }
}
