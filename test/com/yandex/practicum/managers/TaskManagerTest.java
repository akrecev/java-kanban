package com.yandex.practicum.managers;

import com.yandex.practicum.exeptions.TaskValidationException;
import com.yandex.practicum.tasks.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;


    @Test
    void shouldGetTaskList() {
        taskManager.delAllTasks();
        task = new Task(TypeTask.TASK, "Task1", "Description Task1", Status.NEW, 300L,
                LocalDateTime.of(2022, 10, 7, 9, 0));
        taskManager.addTask(task);
        Task task2 = new Task(TypeTask.TASK, "Task2", "Description Task2", Status.NEW, 300L,
                LocalDateTime.of(2022, 10, 8, 9, 0));
        taskManager.addTask(task2);

        assertNotNull(taskManager.getTaskList(), "Список задач отсутствует");
        assertFalse(taskManager.getTaskList().isEmpty(), "Список задач пуст");
        assertEquals(taskManager.getTaskList().toString(), List.of(task, task2).toString());
    }

    @Test
    void shouldGetTaskListEmpty() {
        assertNotNull(taskManager.getTaskList(), "Список задач отсутствует");
        assertFalse(taskManager.getTaskList().isEmpty(), "Список задач пуст до удаления");

        taskManager.delAllTasks();

        assertTrue(taskManager.getTaskList().isEmpty(),
                "Список задач не пуст после удаления всех задач");
    }

    @Test
    void shouldDelAllTasks() {
        taskManager.delAllTasks();

        assertNotNull(taskManager.getTaskList(), "Список задач отсутствует");
        assertTrue(taskManager.getTaskList().isEmpty(), "Список задач не пуст");
    }

    @Test
    void shouldGetTaskById() {
        task = taskManager.getTaskById(1);
        Task taskTest = new Task(TypeTask.TASK, "Task1", "Description Task1", 1, Status.NEW, 600L,
                LocalDateTime.of(2022, 10, 20, 20, 0));

        assertNotNull(task, "Задача не получена");
        assertEquals(task.toString(), taskTest.toString(), "получена другая задача");
    }

    @Test
    void shouldGetTaskByIdEmptyTaskList() {
        taskManager.delAllTasks();

        assertNull(taskManager.getTaskById(1), "Список задач не пуст");
    }

    @Test
    void shouldAddTask() {
        taskManager.delAllTasks(); // предварительная очистка списка задач
        // создание и запись задачи
        Task task = new Task(TypeTask.TASK, "Task1", "Description Task1", 1, Status.NEW, 600L,
                LocalDateTime.of(2022, 10, 20, 20, 0));
        final int taskId = taskManager.addTask(task);
        // копирование задачи
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldDeleteTaskById() {
        Task task2 = new Task(TypeTask.TASK, "task2", "description task2", 4, Status.NEW);
        taskManager.addTask(task2);

        taskManager.deleteTaskById(1);

        assertFalse(taskManager.getTaskList().contains(task), "Задача task не удалена");

        taskManager.deleteTaskById(4);

        assertFalse(taskManager.getTaskList().contains(task2), "Задача task2 не удалена");
    }

    @Test
    void shouldSetEpicStatusNEWEmpty() {
        taskManager.delAllSubtask();

        assertNotNull(taskManager.getEpicList().get(0), "нет эпиков");
        assertTrue(taskManager.getSubtaskList().isEmpty(), "список подзадач не пуст");
        assertEquals(Status.NEW, taskManager.getEpicList().get(0).getStatus());
    }

    @Test
    void shouldSetEpicStatusNEW() {
        taskManager.delAllSubtask();
        taskManager.delAllEpics();

        epic = new Epic(TypeTask.EPIC, "epic", "description epic", 2, Status.DONE);
        taskManager.addEpic(epic);

        assertEquals(Status.NEW, taskManager.getEpicList().get(0).getStatus());

    }

    @Test
    void shouldSetEpicStatusNEWAllSubtaskNEW() {
        Subtask subtask2 = new Subtask(TypeTask.SUBTASK, "subtask2", "description subtask2", 3,
                Status.NEW, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask(TypeTask.SUBTASK, "subtask3", "description subtask3", 3,
                Status.NEW, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask3);

        assertNotNull(taskManager.getEpicList().get(0), "нет эпиков");
        assertFalse(taskManager.getSubtaskList().isEmpty(), "список подзадач пуст");
        assertEquals(Status.NEW, taskManager.getEpicList().get(0).getStatus());
    }

    @Test
    void shouldSetEpicStatusDONEAllSubtaskDONE() {
        taskManager.delAllSubtask();

        Subtask subtask = new Subtask(TypeTask.SUBTASK, "subtask", "description subtask", 3,
                Status.DONE, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask);
        Subtask subtask2 = new Subtask(TypeTask.SUBTASK, "subtask2", "description subtask2", 3,
                Status.DONE, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask(TypeTask.SUBTASK, "subtask3", "description subtask3", 3,
                Status.DONE, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask3);

        assertNotNull(taskManager.getEpicList().get(0), "нет эпиков");
        assertFalse(taskManager.getSubtaskList().isEmpty(), "список подзадач пуст");
        assertEquals(Status.DONE, taskManager.getEpicList().get(0).getStatus());
    }

    @Test
    void shouldSetEpicStatusIN_PROGRESSSubtaskNEWEndDONE() {
        taskManager.delAllSubtask();

        Subtask subtask = new Subtask(TypeTask.SUBTASK, "subtask", "description subtask", 3,
                Status.NEW, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask);
        Subtask subtask2 = new Subtask(TypeTask.SUBTASK, "subtask2", "description subtask2", 3,
                Status.DONE, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask(TypeTask.SUBTASK, "subtask3", "description subtask3", 3,
                Status.DONE, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask3);

        assertNotNull(taskManager.getEpicList().get(0), "нет эпиков");
        assertFalse(taskManager.getSubtaskList().isEmpty(), "список подзадач пуст");
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicList().get(0).getStatus());
    }

    @Test
    void shouldSetEpicStatusIN_PROGRESSSubtaskIN_PROGRESS() {
        taskManager.delAllSubtask();

        Subtask subtask = new Subtask(TypeTask.SUBTASK, "subtask", "description subtask", 3,
                Status.IN_PROGRESS, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask);
        Subtask subtask2 = new Subtask(TypeTask.SUBTASK, "subtask2", "description subtask2", 3,
                Status.IN_PROGRESS, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask(TypeTask.SUBTASK, "subtask3", "description subtask3", 3,
                Status.IN_PROGRESS, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask3);

        assertNotNull(taskManager.getEpicList().get(0), "нет эпиков");
        assertFalse(taskManager.getSubtaskList().isEmpty(), "список подзадач пуст");
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicList().get(0).getStatus());
    }

    @Test
    void shouldSetEpicStatusIN_PROGRESSSubtaskVarious() {
        taskManager.delAllSubtask();

        Subtask subtask = new Subtask(TypeTask.SUBTASK, "subtask", "description subtask", 3,
                Status.NEW, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask);
        Subtask subtask2 = new Subtask(TypeTask.SUBTASK, "subtask2", "description subtask2", 3,
                Status.IN_PROGRESS, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask(TypeTask.SUBTASK, "subtask3", "description subtask3", 3,
                Status.DONE, taskManager.getEpicList().get(0).getId());
        taskManager.addSubtask(subtask3);

        assertNotNull(taskManager.getEpicList().get(0), "нет эпиков");
        assertFalse(taskManager.getSubtaskList().isEmpty(), "список подзадач пуст");
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicList().get(0).getStatus());
    }

    @Test
    void shouldGetEpicListEmpty() {
        taskManager.delAllEpics();

        Assertions.assertTrue(taskManager.getEpicList().isEmpty(), "Список эпиков не пуст");
    }

    @Test
    void shouldGetEpicList() {
        taskManager.delAllEpics();

        Epic epic = new Epic(TypeTask.EPIC, "epic2", "description epic", 2, Status.NEW);
        taskManager.addEpic(epic);
        Epic epic2 = new Epic(TypeTask.EPIC, "epic2", "description epic", 2, Status.NEW);
        taskManager.addEpic(epic2);

        assertFalse(taskManager.getEpicList().isEmpty(), "Список эпиков пуст");
        assertEquals(2, taskManager.getEpicList().size());
        assertTrue(taskManager.getEpicList().contains(epic), "Отсутствует эпик");
        assertTrue(taskManager.getEpicList().contains(epic2), "Отсутствует эпик2");
    }

    @Test
    void shouldDelAllEpics() {
        assertFalse(taskManager.getEpicList().isEmpty(), "Список эпиков пуст пуст до удаления");

        taskManager.delAllEpics();

        assertTrue(taskManager.getEpicList().isEmpty(), "Список эпиков не пуст после удаления");
    }

    @Test
    void shouldGetEpicById() {
        assertNotNull(taskManager.getEpicById(2), "эпика с id=2 нет");
        assertEquals("Epic1", taskManager.getEpicById(2).getTitle(), "получена другая задача");
    }

    @Test
    void shouldAddEpic() {
        taskManager.delAllEpics();
        assertTrue(taskManager.getEpicList().isEmpty(), "Список эпиков не очищен перед тестом");

        epic = new Epic(TypeTask.EPIC, "epic", "description epic", 2, Status.NEW);
        taskManager.addEpic(epic);

        assertEquals(epic.getId(), taskManager.getEpicList().get(0).getId(), "эпик не добавлен");
    }

    @Test
    void shouldDeleteEpicById() {
        assertFalse(taskManager.getEpicList().isEmpty(), "Список эпиков пуст перед тестом");

        taskManager.deleteEpicById(2);

        assertTrue(taskManager.getEpicList().isEmpty(), "Эпик не удален");
    }

    @Test
    void shouldUpdateSubtask() {
        subtask = new Subtask(TypeTask.SUBTASK, "subtaskUp", "description subtask", 3,
                Status.NEW, 2);
        taskManager.updateSubtask(subtask);

        assertEquals("subtaskUp",
                taskManager.getSubtaskById(taskManager.getEpicById(2).getSubTasksIds().get(0)).getTitle());
    }

    @Test
    void shouldGetSubtaskListByEpicEmpty() {
        taskManager.delAllSubtask();

        assertTrue(taskManager.getEpicById(2).getSubTasksIds().isEmpty(),
                "Список сабтасков в эпике не пуст после удаления всех сабтасков");
    }

    @Test
    void shouldGetSubtaskListByEpic() {
        assertEquals(3, taskManager.getEpicById(2).getSubTasksIds().get(0),
                "В списке сабтасков в эпике неверное значение");
    }

    @Test
    void shouldGetSubtaskList() {
        taskManager.delAllSubtask();
        assertTrue(taskManager.getSubtaskList().isEmpty(), "Список сабтасков не очищен перед тестом");

        subtask = new Subtask(TypeTask.SUBTASK, "subtask", "description subtask", 3,
                Status.NEW, 2);
        taskManager.addSubtask(subtask);
        Subtask subtask2 = new Subtask(TypeTask.SUBTASK, "subtask2", "description subtask2", 3,
                Status.NEW, 2);
        taskManager.addSubtask(subtask2);

        assertFalse(taskManager.getSubtaskList().isEmpty(), "Список сабтасков пуст");
        assertTrue(taskManager.getEpicById(2).getSubTasksIds().contains(subtask.getId()));
        assertTrue(taskManager.getEpicById(2).getSubTasksIds().contains(subtask2.getId()));
    }

    @Test
    void shouldDelAllSubtask() {
        assertFalse(taskManager.getSubtaskList().isEmpty(), "Список сабтасков пуст перед тестом");

        taskManager.delAllSubtask();

        assertTrue(taskManager.getSubtaskList().isEmpty(), "Список сабтасков не пуст после удаления");
    }

    @Test
    void shouldGetSubtaskByIdEmpty() {
        taskManager.delAllSubtask();

        assertNull(taskManager.getSubtaskById(3), "Не null из пустого спмска сабтасков");
    }

    @Test
    void shouldGetSubtaskById() {
        assertNotNull(taskManager.getSubtaskById(3), "Сабтаска с id=3 нет");
        assertEquals(3, taskManager.getSubtaskById(3).getId(), "получен неверный сабтаск");
    }

    @Test
    void shouldAddSubtask() {
        taskManager.delAllSubtask();
        assertTrue(taskManager.getSubtaskList().isEmpty(), "Список сабтасков не пуст перед тестом");

        subtask = new Subtask(TypeTask.SUBTASK, "subtask", "description subtask", 3,
                Status.NEW, 2);
        taskManager.addSubtask(subtask);

        assertEquals(4, taskManager.getSubtaskList().get(0).getId());
    }

    @Test
    void shouldDeleteSubtaskByIdOneSubtask() {
        taskManager.deleteSubtaskById(3);

        assertTrue(taskManager.getSubtaskList().isEmpty());
        assertTrue(taskManager.getEpicList().get(0).getSubTasksIds().isEmpty());
    }

    @Test
    void shouldDeleteSubtaskByIdTwoSubtask() {
        Subtask subtask2 = new Subtask(TypeTask.SUBTASK, "subtask2", "description subtask2", 4,
                Status.NEW, 2);
        taskManager.addSubtask(subtask2);

        assertEquals(2, taskManager.getSubtaskList().size(), "перед тестом не 2 подзадачи");
        assertEquals(2, taskManager.getEpicList().get(0).getSubTasksIds().size(),
                "перед тестом в эпике не 2 подзадачи");

        taskManager.deleteSubtaskById(3);

        assertEquals(1, taskManager.getSubtaskList().size(),
                "после удаления осталась не одна задача");
        assertEquals(1, taskManager.getEpicList().get(0).getSubTasksIds().size(),
                "после удаления в эпике осталась не одна задача");
    }

    @Test
    void shouldGetHistory() {
        assertEquals(List.of(taskManager.getEpicById(2), taskManager.getSubtaskById(3)).toString(),
                taskManager.getHistory().toString(), "перед выполнением теста не загружена история");

        taskManager.getTaskById(1);

        assertEquals(List.of(taskManager.getTaskById(1), taskManager.getEpicById(2),
                        taskManager.getSubtaskById(3)).toString(), taskManager.getHistory().toString(),
                "неверно отображена история просмотров");

        Task task2 = new Task(TypeTask.TASK, "task2", "description task2", 4, Status.NEW, 600L,
                LocalDateTime.of(2022, 6, 20, 20, 0));
        taskManager.addTask(task2);

        assertEquals(List.of(taskManager.getTaskById(4), taskManager.getTaskById(1),
                        taskManager.getEpicById(2), taskManager.getSubtaskById(3)).toString(),
                taskManager.getHistory().toString(), "неверно отображена история просмотров");
    }

    @Test
    void shouldGetHistoryAfterDelete() {
        assertEquals(List.of(taskManager.getEpicById(2), taskManager.getSubtaskById(3)).toString(),
                taskManager.getHistory().toString(), "перед выполнением теста не загружена история");

        taskManager.getTaskById(1);
        Task task2 = new Task(TypeTask.TASK, "task2", "description task2", 4, Status.NEW, 600L,
                LocalDateTime.of(2022, 3, 20, 20, 0));
        taskManager.addTask(task2);

        assertEquals(List.of(taskManager.getTaskById(4), taskManager.getTaskById(1),
                        taskManager.getEpicById(2), taskManager.getSubtaskById(3)).toString(),
                taskManager.getHistory().toString(),"неверно отображена история просмотров");

        taskManager.deleteTaskById(1);

        assertEquals(List.of(taskManager.getTaskById(4), taskManager.getEpicById(2), taskManager.getSubtaskById(3)).toString(),
                taskManager.getHistory().toString(), "неверно отображена история просмотров после удаления");
        taskManager.deleteSubtaskById(3);
        assertEquals(List.of(taskManager.findTask(2), taskManager.getTaskById(4)).toString(),
                taskManager.getHistory().toString(), "неверно отображена история просмотров после удаления");

        taskManager.deleteTaskById(4);
        taskManager.deleteEpicById(2);

        assertTrue(taskManager.getHistory().isEmpty(), "история не пуста после удаления всех задач");
    }

    @Test
    void shouldFindTask() {
        assertEquals(taskManager.getTaskById(1), taskManager.findTask(1),
                "задача найдена неверно");
    }

    @Test
    void shouldTaskValidationException() {
        Task task1 = new Task(TypeTask.TASK, "Task1", "Description Task1", Status.NEW, 300L,
                LocalDateTime.of(2022, 10, 7, 9, 0));
        Task task2 = new Task(TypeTask.TASK, "Task1", "Description Task1", Status.NEW, 300L,
                LocalDateTime.of(2022, 10, 7, 9, 0));

        final TaskValidationException exception = assertThrows(
                TaskValidationException.class,
                () -> {
                    taskManager.addTask(task1);
                    taskManager.addTask(task2);
                }
        );

        assertEquals("Новая задача " + task2.getTitle() + " пересекается с существующей: "
                + task1.getTitle(), exception.getMessage());
    }

}