package com.yandex.practicum.managers;

import com.yandex.practicum.exeptions.ManagerSaveException;
import com.yandex.practicum.tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    public FileBackedTasksManager() {
    }

    // загрузка TasksManager'а из файла после запуска программы
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager();
        try {
            String data = Files.readString(file.toPath());
            String[] lines = data.split(System.lineSeparator());
            List<Integer> history = Collections.emptyList();
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isBlank()) {
                    history = CsvFormatter.historyFromString(lines[i + 1]);
                    break;
                }
                Task task = CsvFormatter.fromString(line);
                if (task != null) {
                    int id = task.getId();
                    if (id > tasksManager.generateId) {
                        tasksManager.generateId = id;
                    }
                    switch (task.getTypeTask()) {
                        case TASK:
                            tasksManager.tasks.put(task.getId(), task);
                            tasksManager.prioritizedTasks.add(task);
                            break;
                        case EPIC:
                            tasksManager.epics.put(task.getId(), (Epic) task);
                            tasksManager.updateEpicDurationAndStartEndTime(task.getId());
                            break;
                        case SUBTASK:
                            tasksManager.subtasks.put(task.getId(), (Subtask) task);
                            tasksManager.prioritizedTasks.add(task);
                            break;
                    }
                    tasksManager.generateId++;
                }
            }

            // восстановление списка subtask'ов у epic'а
            for (Subtask subtask : tasksManager.subtasks.values()) {
                Epic epic = tasksManager.epics.get(subtask.getEpicId());
                epic.getSubTasksIds().add(subtask.getId());
                tasksManager.updateEpicDurationAndStartEndTime(subtask.getEpicId());
            }

            // восстановление истории
            for (Integer taskId : history) {
                tasksManager.historyManager.add(tasksManager.findTask(taskId));
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        return tasksManager;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter("save.csv");
             BufferedWriter br = new BufferedWriter(fileWriter)) {
            br.write(CsvFormatter.getHeader() + System.lineSeparator());
            for (Task task : tasks.values()) {
                br.write(CsvFormatter.toStringTask(task) + System.lineSeparator());
            }
            for (Epic epic : epics.values()) {
                br.write(CsvFormatter.toStringEpic(epic) + System.lineSeparator());
            }
            for (Subtask subtask : subtasks.values()) {
                br.write(CsvFormatter.toStringSubtask(subtask) + System.lineSeparator());
            }
            br.write(System.lineSeparator());
            br.write(CsvFormatter.historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла");
        }
    }

    @Override
    public void delAllTasks() {
        super.delAllTasks();
        save();
    }

    @Override
    public int addTask(Task task) {
        super.addTask(task);
        save();
        return task.getId();
    }

    @Override
    public int updateTask(Task task) {
        super.updateTask(task);
        save();
        return task.getId();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void delAllEpics() {
        super.delAllEpics();
        save();
    }

    @Override
    public int addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void delAllSubtask() {
        super.delAllSubtask();
        save();
    }

    @Override
    public int addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public int updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

}
