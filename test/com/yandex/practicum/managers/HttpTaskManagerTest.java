package com.yandex.practicum.managers;

import com.yandex.practicum.server.KVServer;
import com.yandex.practicum.tasks.Epic;
import com.yandex.practicum.tasks.Subtask;
import com.yandex.practicum.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private KVServer kvServer;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new HttpTaskManager(KVServer.PORT);
        kvServer = Managers.getDefaultKVServer();
//        task = new Task(TypeTask.TASK, "Task1", "Description Task1", Status.NEW, 10L,
//                LocalDateTime.now());
//        taskManager.addTask(task);
//        epic = new Epic(TypeTask.EPIC, "Epic1", "Description Epic1", Status.NEW);
//        taskManager.addEpic(epic);
//        subtask = new Subtask(TypeTask.SUBTASK, "Subtask1", "Description Subtask1",
//                Status.NEW, epic.getId(), 120L,
//                task.getEndTime().plusMinutes(10L));
//        taskManager.addSubtask(subtask);
//        kvServer.start();
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }
}