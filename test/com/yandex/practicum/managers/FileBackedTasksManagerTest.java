package com.yandex.practicum.managers;

import com.yandex.practicum.exeptions.ManagerSaveException;
import com.yandex.practicum.tasks.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    @BeforeEach
    void setUp() {
        File file = new File("dataTest.csv");
        super.taskManager = FileBackedTasksManager.loadFromFile(file);
    }


    @Test
    void shouldLoadFromFileEmpty() {
        File file = new File("dataTestEmpty.csv");

        super.taskManager = FileBackedTasksManager.loadFromFile(file);

        assertNotNull(taskManager.tasks, "список тасков не создан");
        assertNotNull(taskManager.epics, "список эпиков не создан");
        assertNotNull(taskManager.subtasks, "список сабтасков не создан");
        assertTrue(taskManager.tasks.isEmpty(), "список тасков не пуст");
        assertTrue(taskManager.epics.isEmpty(), "список эпиков не пуст");
        assertTrue(taskManager.subtasks.isEmpty(), "список сабтасков не пуст");
        assertTrue(taskManager.getHistory().isEmpty(), "история не пуста");
    }

    @Test
    void shouldLoadFromFileBigID() {
        File file = new File("dataTestBigID.csv");
        Task task2 = new Task(TypeTask.TASK, "task2", "description task2", 1, Status.NEW);

        taskManager.addTask(task2);
        super.taskManager = FileBackedTasksManager.loadFromFile(file);

        assertEquals(100, taskManager.getTaskList().get(0).getId(),
                "ID таска не соответствует значению в загрузочном файле");
        assertEquals(301, taskManager.getTaskList().get(1).getId(),
                "ID таска2 назначен не верно");
    }

    @Test
    void shouldLoadFromFileBadType() {
        File file = new File("dataTestBadType.csv");

        super.taskManager = FileBackedTasksManager.loadFromFile(file);

        assertNotNull(taskManager.tasks, "список тасков не создан");
        assertNotNull(taskManager.epics, "список эпиков не создан");
        assertNotNull(taskManager.subtasks, "список сабтасков не создан");
        assertNull(taskManager.findTask(1), "создан таск с неправильным типом");
        assertNull(taskManager.findTask(2), "создан эпик с неправильным типом");
        assertNull(taskManager.findTask(3), "создан сабтаск с неправильным типом");
    }

    @Test
    void shouldLoadFromFileEpicEmpty() {
        File file = new File("dataTestEpicEmpty.csv");

        super.taskManager = FileBackedTasksManager.loadFromFile(file);

        assertNotNull(taskManager.tasks, "список тасков не создан");
        assertNotNull(taskManager.epics, "список эпиков не создан");
        assertNotNull(taskManager.subtasks, "список сабтасков не создан");
        assertEquals(1, taskManager.getTaskList().get(0).getId());
        assertEquals(2, taskManager.getEpicList().get(0).getId());
        Assertions.assertTrue(taskManager.subtasks.isEmpty(), "список сабтасков не пуст");
    }

    @Test
    void shouldLoadFromFileEmptyHistory() {
        File file = new File("dataTestEmptyHistory.csv");

        super.taskManager = FileBackedTasksManager.loadFromFile(file);

        assertNotNull(taskManager.tasks, "список тасков не создан");
        assertNotNull(taskManager.epics, "список эпиков не создан");
        assertNotNull(taskManager.subtasks, "список сабтасков не создан");
        assertEquals(1, taskManager.getTaskList().get(0).getId(), "загружен неверный таск");
        assertEquals(2, taskManager.getEpicList().get(0).getId(), "загружен неверный эпик");
        assertEquals(3, taskManager.getSubtaskList().get(0).getId(),
                "загружен неверный сабтаск");
        Assertions.assertTrue(taskManager.getHistory().isEmpty(), "история не пуста");
    }

    @Test
    void shouldLoadFromFileNotFound() {
        File file = new File("XXX.csv");

        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> taskManager = FileBackedTasksManager.loadFromFile(file)
        );

        assertEquals("Ошибка чтения файла", exception.getMessage());
    }

    @Test
    void shouldSaveToFile() {
        String data;
        String dataSave;
        File file = new File("dataTest.csv");

        super.taskManager = FileBackedTasksManager.loadFromFile(file);
        taskManager.save();
        try {
            data = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        File fileSave = new File("save.csv");
        try {
            dataSave = Files.readString(fileSave.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }

        assertEquals(data, dataSave, "содержимое исходного и сохраненного файлов не совпадают");
    }

    @Test
    void shouldSaveToFileEmpty() {
        String data;
        String dataSave;
        File file = new File("dataTestEmpty.csv");

        super.taskManager = FileBackedTasksManager.loadFromFile(file);
        taskManager.save();
        try {
            data = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        File fileSave = new File("save.csv");
        try {
            dataSave = Files.readString(fileSave.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }

        assertEquals(data, dataSave, "содержимое исходного и сохраненного файлов не совпадают");
    }

    @Test
    void shouldSaveToFileBigID() {
        String data;
        String dataSave;
        File file = new File("dataTestBigID.csv");

        super.taskManager = FileBackedTasksManager.loadFromFile(file);
        taskManager.save();
        try {
            data = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        File fileSave = new File("save.csv");
        try {
            dataSave = Files.readString(fileSave.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }

        assertEquals(data, dataSave, "содержимое исходного и сохраненного файлов не совпадают");
    }

    @Test
    void shouldSaveToFileEpicEmpty() {
        String data;
        String dataSave;
        File file = new File("dataTestEpicEmpty.csv");

        super.taskManager = FileBackedTasksManager.loadFromFile(file);
        taskManager.save();
        try {
            data = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        File fileSave = new File("save.csv");
        try {
            dataSave = Files.readString(fileSave.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }

        assertEquals(data, dataSave, "содержимое исходного и сохраненного файлов не совпадают");
    }

    @Test
    void shouldSaveToFileEmptyHistory() {
        String data;
        String dataSave;
        File file = new File("dataTestEmptyHistory.csv");

        super.taskManager = FileBackedTasksManager.loadFromFile(file);
        taskManager.save();
        try {
            data = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        File fileSave = new File("save.csv");
        try {
            dataSave = Files.readString(fileSave.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }

        assertEquals(data, dataSave, "содержимое исходного и сохраненного файлов не совпадают");
    }

    @Test
    void shouldSaveToFileNoDownload() {
        // создание экземпляра FileBackedTasksManager taskManagerLoad без загрузки данных из файла
        FileBackedTasksManager taskManagerLoad = new FileBackedTasksManager();
        // создание задач и добавление их в taskManagerLoad
        task = new Task(TypeTask.TASK, "Task1", "Description Task1", Status.NEW, 600L,
                LocalDateTime.of(2022, 10, 20, 20, 0));
        taskManagerLoad.addTask(task);
        epic = new Epic(TypeTask.EPIC, "Epic1", "Description Epic1", Status.NEW);
        taskManagerLoad.addEpic(epic);
        subtask = new Subtask(TypeTask.SUBTASK, "Subtask1", "Description Subtask1",
                Status.NEW, epic.getId(), 120L,
                LocalDateTime.of(2022, 10, 21, 20, 0));
        taskManagerLoad.addSubtask(subtask);

        // вызов задач для создания истории
        taskManagerLoad.getTaskById(1);
        taskManagerLoad.getEpicById(2);
        taskManagerLoad.getSubtaskById(3);
        taskManagerLoad.save();
        // запись в файл save.csv
        File fileSave = new File("save.csv");
        taskManager = FileBackedTasksManager.loadFromFile(fileSave);

        // вызов тестов
        assertEquals(taskManagerLoad.getTaskList().get(0).getId(), taskManager.getTaskList().get(0).getId());
        assertEquals(taskManagerLoad.getEpicList().get(0).getId(), taskManager.getEpicList().get(0).getId());
        assertEquals(taskManagerLoad.getSubtaskList().get(0).getId(),
                taskManager.getSubtaskList().get(0).getId());
        assertEquals(taskManagerLoad.getHistory().toString(), taskManager.getHistory().toString());
    }

}