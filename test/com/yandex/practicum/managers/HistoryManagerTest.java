package com.yandex.practicum.managers;

import com.yandex.practicum.tasks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    HistoryManager historyManager;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task = new Task(TypeTask.TASK, "task", "description task", 1, Status.NEW);
        epic = new Epic(TypeTask.EPIC, "epic", "description epic", 2, Status.NEW);
        subtask = new Subtask(TypeTask.SUBTASK, "subtask", "description subtask", 3, Status.NEW,
                epic.getId());
    }

    @Test
    void shouldAddToHistory() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(1, history.size(), "Размер истории отличается от 1");
    }

    @Test
    void shouldHistoryIsEmpty() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertTrue(history.isEmpty(), "Список истории не пуст");
        historyManager.add(task);
        history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(1, history.size(), "Размер истории отличается от 1");
        historyManager.remove(1);
        history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertTrue(history.isEmpty(), "Список истории не пуст");
    }

    @Test
    void shouldDuplicatingHistoryOneValue() {
        historyManager.add(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(1, history.size(), "Размер истории отличается от 1");
    }

    @Test
    void shouldRemoveHistoryFirst() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(3, history.size(), "Размер истории отличается от 3");

        historyManager.remove(1);
        history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(2, history.size(), "Размер истории отличается от 2 после удаления");
        assertEquals(epic, history.get(0), "Нарушен порядок добавления");
        assertEquals(subtask, history.get(1), "Нарушен порядок добавления");
    }

    @Test
    void shouldRemoveHistoryMiddle() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(3, history.size(), "Размер истории отличается от 3");

        historyManager.remove(2);
        history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(2, history.size(), "Размер истории отличается от 2 после удаления");
        assertEquals(task, history.get(0), "Нарушен порядок добавления");
        assertEquals(subtask, history.get(1), "Нарушен порядок добавления");
    }

    @Test
    void shouldRemoveHistoryLast() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(3, history.size(), "Размер истории отличается от 3");

        historyManager.remove(3);
        history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertEquals(2, history.size(), "Размер истории отличается от 2 после удаления");
        assertEquals(task, history.get(0), "Нарушен порядок добавления");
        assertEquals(epic, history.get(1), "Нарушен порядок добавления");
    }


}