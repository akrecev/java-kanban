package com.yandex.practicum.managers;

import com.yandex.practicum.server.KVServer;
import com.yandex.practicum.tasks.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private KVServer kvServer;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() throws IOException {
        kvServer = Managers.getDefaultKVServer();
        kvServer.start();
        taskManager = new HttpTaskManager(KVServer.PORT);
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
    }

    @Test
    void saveTest() {
        TaskManager taskManagerLoad = FileBackedTasksManager.loadFromFile(new File("data.csv"));
        Task taskLoad = taskManagerLoad.getTaskById(1);
        taskManager.addTask(taskLoad);
        Epic epicLoad = taskManagerLoad.getEpicById(2);
        taskManager.addEpic(epicLoad);
        Subtask subtaskLoad = taskManagerLoad.getSubtaskById(3);
        taskManager.addSubtask(subtaskLoad);

        taskManager.save();

        assertEquals(taskManagerLoad.getTaskById(1), taskManager.getTaskById(1), "Задачи не совпадают");
        assertEquals(taskManagerLoad.getEpicById(2), taskManager.getEpicById(2), "Эпики не совпадают");
        assertEquals(taskManagerLoad.getSubtaskById(3), taskManager.getSubtaskById(3), "Подзадачи не совпадают");
    }

    @Test
    void loadTest() {
        task = new Task(TypeTask.TASK, "Task1", "Description Task1", 101, Status.NEW, 300L,
                LocalDateTime.now());
        taskManager.addTask(task);
        epic = new Epic(TypeTask.EPIC, "Epic1", "Description Epic1", 102, Status.NEW);
        taskManager.addEpic(epic);
        subtask = new Subtask(TypeTask.SUBTASK, "subtask", "description subtask", 103,
                Status.IN_PROGRESS, epic.getId(), 20L, task.getEndTime());
        taskManager.addSubtask(subtask);

        taskManager.save();

        TaskManager taskManagerSave = Managers.getDefault();
        taskManagerSave.addTask(task);
        taskManagerSave.addEpic(epic);
        taskManagerSave.addSubtask(subtask);

        assertFalse(taskManager.getTasks().isEmpty());
        assertFalse(taskManager.getEpics().isEmpty());
        assertFalse(taskManager.getSubtasks().isEmpty());

        taskManager.setSave(false); // отключение сохранения изменений на сервере
        taskManager.delAllTasks();
        taskManager.delAllEpics();
        taskManager.delAllSubtask();

        assertTrue(taskManager.getTasks().isEmpty());
        assertTrue(taskManager.getEpics().isEmpty());
        assertTrue(taskManager.getSubtasks().isEmpty());

        taskManager.setSave(true); // включение сохранения изменений на сервере

        taskManager.load();

        assertFalse(taskManager.getTasks().isEmpty());
        assertFalse(taskManager.getEpics().isEmpty());
        assertFalse(taskManager.getSubtasks().isEmpty());

        assertEquals(taskManagerSave.getTaskById(101), taskManager.getTaskById(101), "Задачи не совпадают");
        assertEquals(taskManagerSave.getEpicById(102), taskManager.getEpicById(102), "Эпики не совпадают");
        assertEquals(taskManagerSave.getSubtaskById(103), taskManager.getSubtaskById(103),
                "Подзадачи не совпадают");
    }

    @Test
    void loadHistoryTest() {
        task = new Task(TypeTask.TASK, "Task1", "Description Task1", 1, Status.NEW, 300L,
                LocalDateTime.now());
        taskManager.addTask(task);
        epic = new Epic(TypeTask.EPIC, "Epic1", "Description Epic1", 2, Status.NEW);
        taskManager.addEpic(epic);
        subtask = new Subtask(TypeTask.SUBTASK, "subtask", "description subtask", 3,
                Status.IN_PROGRESS, epic.getId(), 20L, task.getEndTime());
        taskManager.addSubtask(subtask);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(3);
        taskManager.getEpicById(2);
        taskManager.save();

        assertEquals(List.of(1, 3, 2), taskManager.getHistoryIds());

        assertEquals(3, taskManager.getHistory().size());

        taskManager.setSave(false); // отключение сохранения изменений на сервере
        taskManager.historyManager.remove(1);
        taskManager.historyManager.remove(2);
        taskManager.historyManager.remove(3);

        assertTrue(taskManager.getHistory().isEmpty());

        taskManager.setSave(true); // включение сохранения изменений на сервере
        taskManager.load();

        assertEquals(List.of(1, 3, 2), taskManager.getHistoryIds());
    }
}