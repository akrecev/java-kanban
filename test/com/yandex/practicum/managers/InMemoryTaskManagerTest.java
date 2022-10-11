package com.yandex.practicum.managers;

import com.yandex.practicum.tasks.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @BeforeEach
    void setUp() {
        super.taskManager = new InMemoryTaskManager();
        task = new Task(TypeTask.TASK, "task", "description task", 1, Status.NEW);
        taskManager.addTask(task);
        epic = new Epic(TypeTask.EPIC, "epic", "description epic", 2, Status.NEW);
        taskManager.addEpic(epic);
        subtask = new Subtask(TypeTask.SUBTASK, "subtask", "description subtask", 3,
                Status.NEW, epic.getId());
        taskManager.addSubtask(subtask);
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