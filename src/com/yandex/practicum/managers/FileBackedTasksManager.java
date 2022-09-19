package com.yandex.practicum.managers;

import com.yandex.practicum.tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public static void main(String[] args) {
        System.out.println("Поехали!");
        File file = new File("data.csv");
        FileBackedTasksManager fileBackedTasksManager = loadFromFile(file);

        Task task2 = new Task(TypeTask.TASK, "Задача-2", "Тестовая задача №2", Status.NEW);
        fileBackedTasksManager.addTask(task2);

        Task task3 = new Task(TypeTask.TASK, "Задача-3", "Тестовая задача №3", Status.NEW);
        fileBackedTasksManager.addTask(task3);

        Epic epic3 = new Epic(TypeTask.EPIC, "Эпик-3", "Тестовый эпик №3", Status.NEW);
        fileBackedTasksManager.addEpic(epic3);

        Subtask subtask3 = new Subtask(TypeTask.SUBTASK, "подзадача-3", "тестовая подзадача №3 к эпику-3", Status.NEW, epic3.getId());
        fileBackedTasksManager.addSubtask(subtask3);


        fileBackedTasksManager.getTaskById(task3.getId());
        fileBackedTasksManager.getTaskById(task2.getId());
        fileBackedTasksManager.getEpicById(epic3.getId());
        fileBackedTasksManager.getSubtaskById(subtask3.getId());

        fileBackedTasksManager.save();

        file = new File("save.csv");

        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(file);

        fileBackedTasksManager2.save();

    }

    // загрузка TasksManager'а из файла после запуска программы
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        try {
            String data = Files.readString(file.toPath());
            String[] lines = data.split(System.lineSeparator());
            List<Integer> history = Collections.emptyList();
            int generateId = 0;
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isBlank()) {
                    history = CSVFormatter.historyFromString(lines[i + 1]);
                    break;
                }
                Task task = CSVFormatter.fromString(line);
                int id = 0;
                if (task != null) {
                    id = task.getId();
                }
                if (id > generateId) {
                    generateId = id;
                }
                if (task != null) {
                    switch (task.getTypeTask()) {
                        case TASK:
                            tasksManager.tasks.put(task.getId(), task);
                            break;
                        case EPIC:
                            tasksManager.epics.put(task.getId(), (Epic) task);
                            break;
                        case SUBTASK:
                            tasksManager.subtasks.put(task.getId(), (Subtask) task);
                            break;
                    }
                }
            }

            // восстановление списка subtask'ов у epic'а
            for (Subtask subtask : tasksManager.subtasks.values()) {
                Epic epic = tasksManager.epics.get(subtask.getEpicId());
                epic.getSubTasksIds().add(subtask.getId());
            }

            // восстановление истории
            for (Integer taskId : history) {
                tasksManager.historyManager.add(tasksManager.findTask(taskId));
            }

            // запись максимального id в генератор
            tasksManager.generateId = generateId;

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        return tasksManager;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter("save.csv");
             BufferedWriter br = new BufferedWriter(fileWriter)) {
            br.write(CSVFormatter.getHeader() + System.lineSeparator());
            for (Task task : tasks.values()) {
                br.write(CSVFormatter.toString(task) + System.lineSeparator());
            }
            for (Task task : epics.values()) {
                br.write(CSVFormatter.toString(task) + System.lineSeparator());
            }
            for (Task task : subtasks.values()) {
                br.write(CSVFormatter.toString(task) + subtasks.get(task.getId()).getEpicId()
                        + System.lineSeparator());
            }
            br.write(System.lineSeparator());
            br.write(CSVFormatter.historyToString(historyManager));
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
