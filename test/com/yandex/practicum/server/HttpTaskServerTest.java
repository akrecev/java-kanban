package com.yandex.practicum.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.practicum.managers.Managers;
import com.yandex.practicum.managers.TaskManager;
import com.yandex.practicum.tasks.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class HttpTaskServerTest {

    private HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private Gson gson = Managers.getGson();

    private Task task;
    private Task task2;
    private Epic epic;
    private Epic epic2;
    private Subtask subtask;
    private Subtask subtask2;
    private Subtask subtask3;
    Map<Integer, Task> tasks;
    Map<Integer, Epic> epics;
    Map<Integer, Subtask> subtasks;

    @BeforeEach
    void setUp() throws IOException {

        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);

        task = new Task(TypeTask.TASK, "Task1", "Description Task1", Status.NEW, 500L,
                LocalDateTime.of(2022, 11, 1, 0, 0));
        taskManager.addTask(task);
        epic = new Epic(TypeTask.EPIC, "Epic1", "Description Epic1", Status.NEW);
        taskManager.addEpic(epic);
        subtask = new Subtask(TypeTask.SUBTASK, "Subtask1", "Description Subtask1",
                Status.NEW, epic.getId(), 120L,
                LocalDateTime.of(2022, 10, 21, 20, 0));
        taskManager.addSubtask(subtask);
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask.getId());

        task2 = new Task(TypeTask.TASK, "Task2", "Description Task2", Status.IN_PROGRESS,
                500L, LocalDateTime.of(2023, 11, 1, 0, 0));
        taskManager.addTask(task2);
        epic2 = new Epic(TypeTask.EPIC, "Epic2", "Description Epic2", Status.NEW);
        taskManager.addEpic(epic2);
        subtask2 = new Subtask(TypeTask.SUBTASK, "Subtask2", "Description Subtask2",
                Status.DONE, epic2.getId(), 120L,
                LocalDateTime.of(2023, 10, 21, 20, 0));
        httpTaskServer.getTaskManager().addSubtask(subtask2);
        subtask3 = new Subtask(TypeTask.SUBTASK, "Subtask3", "Description Subtask3",
                Status.DONE, epic2.getId(), 120L,
                LocalDateTime.of(2023, 1, 21, 20, 0));
        taskManager.addSubtask(subtask3);
        taskManager.getTaskById(task2.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getSubtaskById(subtask2.getId());

        tasks = taskManager.getTasks();
        epics = taskManager.getEpics();
        subtasks = taskManager.getSubtasks();

        httpTaskServer.start();

    }

    @AfterEach
    void tearDown() {
        httpTaskServer.stop();
    }

    @Test
    void shouldGetAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type typeTasks = new TypeToken<Map<Integer, Task>>() {
        }.getType();
        Map<Integer, Task> tasksReceived = gson.fromJson(response.body(), typeTasks);

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertNotNull(tasksReceived, "Список задач не получен");
        assertEquals(tasks.size(), tasksReceived.size(), "Размер полученого списка не соответствует " +
                "количеству задач");
        assertEquals(tasks, tasksReceived, "Получен неверный список задач");
    }

    @Test
    void getHttpServer() {
    }

    @Test
    void getGson() {
    }

    @Test
    void getTaskManager() {
    }

    @Test
    void start() {
    }

    @Test
    void stop() {
    }
}