package com.yandex.practicum.managers;

import com.yandex.practicum.tasks.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @BeforeEach
    void setUp() {
        // создание нового экземпляра InMemoryTaskManager taskManager
        taskManager = new InMemoryTaskManager();
        // создание и запись задач
        task = new Task(TypeTask.TASK, "Task1", "Description Task1", Status.NEW, 600L,
                LocalDateTime.of(2022, 10, 20, 20, 0));
        taskManager.addTask(task);
        epic = new Epic(TypeTask.EPIC, "Epic1", "Description Epic1", Status.NEW);
        taskManager.addEpic(epic);
        subtask = new Subtask(TypeTask.SUBTASK, "Subtask1", "Description Subtask1",
                Status.NEW, epic.getId(), 120L,
                LocalDateTime.of(2022, 10, 21, 20, 0));
        taskManager.addSubtask(subtask);
        // вызов задач для создания истории просмотров
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);
    }

    @Test
    public void shouldCheckEpicStatus() {
        TaskManager taskManager = Managers.getDefault();
        epic = new Epic(TypeTask.EPIC, "epic", "description epic", 2, Status.NEW);
        taskManager.addEpic(epic);

        Assertions.assertEquals(Status.NEW, epic.getStatus());

        subtask = new Subtask(TypeTask.SUBTASK, "subtask", "description subtask", 3,
                Status.IN_PROGRESS, epic.getId());
        taskManager.addSubtask(subtask);

        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}