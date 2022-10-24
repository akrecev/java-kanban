package com.yandex.practicum.managers;

import com.yandex.practicum.exeptions.TaskValidationException;
import com.yandex.practicum.tasks.Epic;
import com.yandex.practicum.tasks.Status;
import com.yandex.practicum.tasks.Subtask;
import com.yandex.practicum.tasks.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected int generateId = 1;

    // таблицы для хранения данных
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    protected final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    /* ------ Методы для задач типа Task ------ */

    // получение списка всех задач
    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    // удаление всех задач
    @Override
    public void delAllTasks() {
        if (tasks.isEmpty()) {
            return;
        }
        tasks.forEach((k, v) -> historyManager.remove(v.getId()));
        tasks.forEach((k, v) -> prioritizedTasks.remove(v));
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
        validate(task);
        task.setId(generateId++);
        return updateTask(task);
    }

    // обновление задачи
    @Override
    public int updateTask(Task task) {
        validate(task);
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        return task.getId();
    }

    // удаление задачи по идентификатору
    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsValue(getTaskById(id))) {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
        }
    }

    /* ------ Методы для задач типа Epic ------ */

    // обновление статуса эпика
    @Override
    public Status epicStatus(Epic epic) {
        if (!epic.getSubTasksIds().isEmpty()) {
            boolean isStatusNEW = true;
            boolean isStatusDONE = true;
            for (int id : epic.getSubTasksIds()) {
                if (subtasks.get(id) == null) {
                    break;
                }
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
        if (epics.isEmpty()) {
            return;
        }
        epics.forEach((k, v) -> historyManager.remove(v.getId()));
        subtasks.forEach((k, v) -> historyManager.remove(v.getId()));
        epics.clear();
        subtasks.clear();
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
        updateEpicDurationAndStartEndTime(epic.getId());
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

    // получение списка всех подзадач
    @Override
    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtasks.values());
    }

    // удаление всех подзадач
    @Override
    public void delAllSubtask() {
        if (subtasks.isEmpty()) {
            return;
        }
        for (Subtask subtask : subtasks.values()) {
            epics.get(subtask.getEpicId()).getSubTasksIds().clear();
        }
        subtasks.forEach((k, v) -> historyManager.remove(v.getId()));
        subtasks.forEach((k, v) -> prioritizedTasks.remove(v));
        subtasks.clear();
        epics.forEach((k, v) -> v.setStatus(epicStatus(v)));
        epics.forEach((k, v) -> updateEpicDurationAndStartEndTime(v.getId()));
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
        validate(subtask);
        subtask.setId(generateId++); // установка id подзадаче
        subtasks.put(subtask.getId(), subtask); // добавление подзадачи в таблицу подзадач
        epics.get(subtask.getEpicId()).getSubTasksIds().add(subtask.getId()); // добавление id подзадачи в список эпика
        checkEpicStatus(subtask); // проверка статуса эпика
        updateEpicDurationAndStartEndTime(subtask.getEpicId()); // обновление времени эпика
        prioritizedTasks.add(subtask);
        return subtask.getId();
    }

    // обновление подзадачи
    @Override
    public int updateSubtask(Subtask subtask) {
        validate(subtask);
        subtasks.put(subtask.getId(), subtask); // замена подзадачи в таблице подзадач
        checkEpicStatus(subtask); // проверка статуса эпика
        updateEpicDurationAndStartEndTime(subtask.getEpicId());
        prioritizedTasks.add(subtask);
        return subtask.getId();
    }

    // удаление подзадачи по идентификатору
    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Epic currentEpic = epics.get(subtasks.get(id).getEpicId());
            historyManager.remove(id);
            prioritizedTasks.remove(subtasks.get(id));
            subtasks.remove(id);
            for (int i = 0; i < currentEpic.getSubTasksIds().size(); i++) {
                if (currentEpic.getSubTasksIds().get(i) == id) {
                    currentEpic.getSubTasksIds().remove(i);
                }
            }
            currentEpic.setStatus(epicStatus(currentEpic));
            updateEpicDurationAndStartEndTime(currentEpic.getId());
        }
    }

    // получение истории просмотров задач
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // получение истории просмотров задач - список из id
    @Override
    public List<Integer> getHistoryIds() {
        return historyManager.getHistoryIds();
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

    // получение списка задач в порядке приоритета
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public List<Integer> getPrioritizedTasksIds() {
        List<Integer> list = new ArrayList<>();
        prioritizedTasks.forEach(v -> list.add(v.getId()));
        return list;
    }


    // проверка статуса эпика при изменении подзадачи
    private void checkEpicStatus(Subtask subtask) {
        Epic currentEpic = epics.get(subtask.getEpicId());
        currentEpic.setStatus(epicStatus(currentEpic));
    }

    // обновление времени эпика
    protected void updateEpicDurationAndStartEndTime(int epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> subTasksIds = epic.getSubTasksIds();

        if (subTasksIds.isEmpty()) {
            epic.setDuration(0L);
            return;
        }

        LocalDateTime startEpic = LocalDateTime.MAX;
        LocalDateTime endEpic = LocalDateTime.MIN;
        long durationEpic = 0L;

        for (int subTasksId : subTasksIds) {
            Subtask subtask = subtasks.get(subTasksId);
            if (subtasks.get(subTasksId) == null) {
                break;
            }
            LocalDateTime startTime = subtask.getStartTime();
            LocalDateTime endTime = subtask.getEndTime();
            if (startTime.isBefore(startEpic)) {
                startEpic = startTime;
            }
            if (endTime.isAfter(endEpic)) {
                endEpic = endTime;
            }
            durationEpic += subtask.getDuration();
        }
        epic.setDuration(durationEpic);
        epic.setStartTime(startEpic);
        epic.setEndTime(endEpic);
    }

    private void validate(Task task) {
        List<Task> prioritizedTasksList = getPrioritizedTasks();
        for (Task currentTask : prioritizedTasksList) {
            if (currentTask.getStartTime() == LocalDateTime.MAX) {
                return;
            }
            if (currentTask.getId() == task.getId()) {
                return;
            }
            boolean isValid = (task.getStartTime().isBefore(currentTask.getStartTime())
                    && task.getEndTime().isBefore(currentTask.getEndTime()))
                    || (task.getStartTime().isAfter(currentTask.getStartTime())
                    && task.getEndTime().isAfter(currentTask.getEndTime()));
            if (!isValid) {
                throw new TaskValidationException("Новая задача " + task.getTitle() + " пересекается с существующей: "
                        + currentTask.getTitle());
            }
        }
    }

}
