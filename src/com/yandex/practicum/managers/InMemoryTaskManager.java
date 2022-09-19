package com.yandex.practicum.managers;

import com.yandex.practicum.tasks.Epic;
import com.yandex.practicum.tasks.Status;
import com.yandex.practicum.tasks.Subtask;
import com.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected int generateId = 1;

    // таблицы для хранения данных
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();

    /* ------ Методы для задач типа Task ------ */

    // получение списка всех задач
    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    // удаление всех задач
    @Override
    public void delAllTasks() {
        tasks.forEach((k, v) -> historyManager.remove(v.getId()));
        tasks.clear();
    }

    // получение задачи по id
    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.getOrDefault(id, null);
    }

    // создание задачи
    @Override
    public int addTask(Task task) {
        task.setId(generateId++);
        return updateTask(task);
    }

    // обновление задачи
    @Override
    public int updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task.getId();
    }

    // удаление задачи по идентификатору
    @Override
    public void deleteTaskById(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    /* ------ Методы для задач типа Epic ------ */

    // обновление статуса эпика
    @Override
    public Status epicStatus(Epic epic) {
        if (epic.getSubTasksIds() == null) {
            return Status.NEW;
        }
        if (!epic.getSubTasksIds().isEmpty()) {
            boolean isStatusNEW = true;
            boolean isStatusDONE = true;
            for (int id : epic.getSubTasksIds()) {
                isStatusNEW &= subtasks.get(id).getStatus().equals(Status.NEW);
                isStatusDONE &= subtasks.get(id).getStatus().equals(Status.DONE);
            }
            if (isStatusNEW) {
                return Status.NEW;
            }
            if (isStatusDONE) {
                return Status.DONE;
            } else {
                return Status.IN_PROGRESS;
            }
        } else {
            return Status.NEW;
        }
    }


    // получение списка эпиков
    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    // удаление всех эпиков
    @Override
    public void delAllEpics() {
        epics.forEach((k, v) -> historyManager.remove(v.getId()));
        subtasks.forEach((k, v) -> historyManager.remove(v.getId()));
        epics.clear();
    }

    // получение эпика по id
    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.getOrDefault(id, null);
    }

    // создание эпика
    @Override
    public int addEpic(Epic epic) {
        epic.setId(generateId++);
        return updateEpic(epic);
    }

    // обновление эпика
    @Override
    public int updateEpic(Epic epic) {
        epic.setStatus(epicStatus(epic));
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    // удаление эпика по идентификатору
    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            for (Integer subTasksId : epics.get(id).getSubTasksIds()) {
                historyManager.remove(subTasksId);
                subtasks.remove(subTasksId);
            }
            historyManager.remove(id);
            epics.remove(id);
        }
    }

    /* ------ Методы для подзадач типа Subtask ------ */

    // Получение списка всех подзадач определенного эпика
    @Override
    public List<Subtask> getSubtaskListByEpic(int id) {
        if (epics.containsKey(id)) {
            List<Subtask> subtaskList = new ArrayList<>();
            for (int i : epics.get(id).getSubTasksIds()) {
                subtaskList.add(subtasks.get(i));
            }
            return subtaskList;
        }
        return null;
    }

    // получение списка всех подзадач
    @Override
    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtasks.values());
    }

    // удаление всех подзадач
    @Override
    public void delAllSubtask() {
        for (Subtask subtask : subtasks.values()) {
            epics.get(subtask.getEpicId()).getSubTasksIds().clear();
        }
        subtasks.forEach((k, v) -> historyManager.remove(v.getId()));
        subtasks.clear();

    }

    // получение подзадачи по id
    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.getOrDefault(id, null);
    }

    // создание подзадачи
    @Override
    public int addSubtask(Subtask subtask) {
        subtask.setId(generateId++); // установка id подзадаче
        subtasks.put(subtask.getId(), subtask); // добавление подзадачи в таблицу подзадач
        epics.get(subtask.getEpicId()).getSubTasksIds().add(subtask.getId()); // добавление id подзадачи в список внутри эпика
        checkEpicStatus(subtask); // проверка статуса эпика
        return subtask.getId();
    }

    // обновление подзадачи
    @Override
    public int updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask); // замена подзадачи в таблице подзадач
        checkEpicStatus(subtask); // проверка статуса эпика
        return subtask.getId();
    }

    // удаление подзадачи по идентификатору
    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Epic currentEpic = epics.get(subtasks.get(id).getEpicId());
            historyManager.remove(id);
            subtasks.remove(id);
            for (int i = 0; i < currentEpic.getSubTasksIds().size(); i++) {
                if (currentEpic.getSubTasksIds().get(i) == id) {
                    currentEpic.getSubTasksIds().remove(i);
                }
            }
            currentEpic.setStatus(epicStatus(currentEpic));
        }
    }

    // получение истории просмотров задач
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // поиск задачи по id
    @Override
    public Task findTask(Integer taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.get(taskId);
        } else if (epics.containsKey(taskId)) {
            return epics.get(taskId);
        } else return subtasks.getOrDefault(taskId, null);
    }

    // проверка статуса эпика при изменении подзадачи
    public void checkEpicStatus(Subtask subtask) {
        Epic currentEpic = epics.get(subtask.getEpicId());
        currentEpic.setStatus(epicStatus(currentEpic));
    }

}
