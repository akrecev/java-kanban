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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class HttpTaskServerTest {

    private HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private final Gson gson = Managers.getGson();

    private Task task;
    private Epic epic;
    private Subtask subtask;
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

        Task task2 = new Task(TypeTask.TASK, "Task2", "Description Task2", Status.IN_PROGRESS,
                500L, LocalDateTime.of(2023, 11, 1, 0, 0));
        taskManager.addTask(task2);
        Epic epic2 = new Epic(TypeTask.EPIC, "Epic2", "Description Epic2", Status.NEW);
        taskManager.addEpic(epic2);
        Subtask subtask2 = new Subtask(TypeTask.SUBTASK, "Subtask2", "Description Subtask2",
                Status.DONE, epic2.getId(), 120L,
                LocalDateTime.of(2023, 10, 21, 20, 0));
        httpTaskServer.getTaskManager().addSubtask(subtask2);
        Subtask subtask3 = new Subtask(TypeTask.SUBTASK, "Subtask3", "Description Subtask3",
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


        /*__________________Task_Tests__________________*/

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
    void shouldGetTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type typeTask = new TypeToken<Task>() {
        }.getType();
        Task taskReceived = gson.fromJson(response.body(), typeTask);

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertNotNull(taskReceived, "Задача не получена");
        assertEquals(task, taskReceived, "Получена неверная задача");
    }

    @Test
    void shouldGetTaskByIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 404");
        assertEquals("Задача с id:2 отсутствует", response.body(), "Неверное тело ответа");
        assertNull(tasks.get(2), "Задача с неверным id найдена в списке");
    }

    @Test
    void shouldUpdateTask() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Task updateTask = new Task(TypeTask.TASK, "Task1", "Description Task1", task.getId(), Status.NEW,
                500L, LocalDateTime.of(2022, 11, 1, 0, 0).plusMinutes(100L));
        String json = gson.toJson(updateTask);
        task.setStartTime(task.getStartTime().plusMinutes(100L));

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(202, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 202");
        assertEquals(task, updateTask, "Получена неверная задача");
    }

    @Test
    void shouldAddTask() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Task newTask = new Task(TypeTask.TASK, "newTask", "Description newTask", 8,
                Status.NEW, 100L, LocalDateTime.of(2022, 11, 12, 0, 0));
        String json = gson.toJson(newTask);

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 201");
        assertEquals(3, tasks.size(), "Размер списка задач неверен");
        assertEquals(taskManager.getTaskList().get(2), newTask, "Добавлена неверная задача");
    }

    @Test
    void shouldDelAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertEquals("Все задачи удалены", response.body(), "Неверное тело ответа");
        assertTrue(tasks.isEmpty(), "Список задач не пуст");
    }

    @Test
    void shouldDelTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertEquals("Задача id:1 удалена", response.body(), "Неверное тело ответа");
        assertNull(tasks.get(1), "Задача с id:1 не удалена из списка");
    }

    @Test
    void shouldDelTaskByIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 404");
        assertEquals("Задача id:2 отсутствует", response.body(), "Неверное тело ответа");
        assertEquals(2, tasks.size(), "Задача удалена по неверному id");
    }

    /*__________________Subtask_Tests__________________*/

    @Test
    void shouldGetAllSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type typeSubtasks = new TypeToken<Map<Integer, Subtask>>() {
        }.getType();
        Map<Integer, Subtask> subtasksReceived = gson.fromJson(response.body(), typeSubtasks);

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertNotNull(subtasksReceived, "Список подзадач не получен");
        assertEquals(subtasks.size(), subtasksReceived.size(), "Размер полученого списка не соответствует " +
                "количеству подзадач");
        assertEquals(subtasks, subtasksReceived, "Получен неверный список подзадач");
    }

    @Test
    void shouldGetSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type typeSubtask = new TypeToken<Subtask>() {
        }.getType();
        Subtask subtaskReceived = gson.fromJson(response.body(), typeSubtask);

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertNotNull(subtaskReceived, "Задача не получена");
        assertEquals(subtask, subtaskReceived, "Получена неверная задача");
    }

    @Test
    void shouldGetSubtaskByIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 404");
        assertEquals("Подзадача с id:2 отсутствует", response.body(), "Неверное тело ответа");
        assertNull(subtasks.get(2), "Подзадача с неверным id найдена в списке");
    }

    @Test
    void shouldUpdateSubtask() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");

        Subtask updateSubtask = new Subtask(TypeTask.SUBTASK, "updateSubtask", "Description updateSubtask",
                subtask.getId(), Status.NEW, epic.getId(), 120L,
                LocalDateTime.of(2022, 10, 21, 20, 0).plusMinutes(100L));
        String json = gson.toJson(updateSubtask);
        subtask.setStartTime(subtask.getStartTime().plusMinutes(100L));

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(202, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 202");
        assertEquals(subtasks.get(3), updateSubtask, "Получена неверная подзадача");
    }

    @Test
    void shouldAddSubtask() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Subtask newSubtask = new Subtask(TypeTask.SUBTASK, "newSubtask", "Description newSubtask", 8,
                Status.NEW, epic.getId(), 20L,
                LocalDateTime.of(2022, 10, 14, 20, 0));
        String json = gson.toJson(newSubtask);

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 201");
        assertEquals(4, subtasks.size(), "Размер списка подзадач неверен");
        assertEquals(taskManager.getSubtaskList().get(3), newSubtask, "Добавлена неверная подзадача");
    }

    @Test
    void shouldDelAllSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertEquals("Все подзадачи удалены", response.body(), "Неверное тело ответа");
        assertTrue(subtasks.isEmpty(), "Список подзадач не пуст");
    }

    @Test
    void shouldDelSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertEquals("Подзадача id:3 удалена", response.body(), "Неверное тело ответа");
        assertNull(subtasks.get(3), "Подадача с id:3 не удалена из списка");
    }

    @Test
    void shouldDelSubtaskByIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 404");
        assertEquals("Подзадача id:2 отсутствует", response.body(), "Неверное тело ответа");
        assertEquals(3, subtasks.size(), "Подзадача удалена по неверному id");
    }

    /*__________________Epic_Tests__________________*/

    @Test
    void shouldGetAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type typeEpics = new TypeToken<Map<Integer, Epic>>() {
        }.getType();
        Map<Integer, Epic> epicsReceived = gson.fromJson(response.body(), typeEpics);

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertNotNull(epicsReceived, "Список эпиков не получен");
        assertEquals(epics.size(), epicsReceived.size(), "Размер полученого списка не соответствует " +
                "количеству эпиков");
        assertEquals(epics, epicsReceived, "Получен неверный список эпиков");
    }

    @Test
    void shouldGetEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type typeEpic = new TypeToken<Epic>() {
        }.getType();
        Epic epicReceived = gson.fromJson(response.body(), typeEpic);

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertNotNull(epicReceived, "Эпик не получен");
        assertEquals(epic, epicReceived, "Получен неверный эпик");
    }

    @Test
    void shouldGetEpicByIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 404");
        assertEquals("Эпик с id:3 отсутствует", response.body(), "Неверное тело ответа");
        assertNull(epics.get(3), "Эпик с неверным id найден в списке");
    }

    @Test
    void shouldUpdateEpic() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");

        Epic updateEpic = new Epic(TypeTask.EPIC, "updateEpic", "Description updateEpic", epic.getId(),
                Status.NEW, epic.getSubTasksIds());
        updateEpic.setDuration(subtasks.get(3).getDuration());
        updateEpic.setStartTime(subtasks.get(3).getStartTime());
        updateEpic.setEndTime(subtasks.get(3).getEndTime());

        String json = gson.toJson(updateEpic);

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(202, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 202");
        assertEquals(epics.get(2), updateEpic, "Получен неверный эпик");
    }

    @Test
    void shouldAddEpic() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Epic newEpic = new Epic(TypeTask.EPIC, "newEpic", "Description newEpic", 8, Status.NEW);

        String json = gson.toJson(newEpic);

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 201");
        assertEquals(3, epics.size(), "Размер списка эпиков неверен");
        assertEquals(taskManager.getEpicList().get(2).toString(), newEpic.toString(), "Добавлен неверный эпик");
    }

    @Test
    void shouldDelAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertEquals("Все эпики удалены", response.body(), "Неверное тело ответа");
        assertTrue(epics.isEmpty(), "Список эпиков не пуст");
    }

    @Test
    void shouldDelEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertEquals("Эпик id:2 удален", response.body(), "Неверное тело ответа");
        assertNull(epics.get(2), "Эпик с id:2 не удален из списка");
    }

    @Test
    void shouldDelEpicByIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 404");
        assertEquals("Эпик id:3 отсутствует", response.body(), "Неверное тело ответа");
        assertEquals(2, epics.size(), "Эпик удален по неверному id");
    }

    @Test
    void shouldGetEpicSubTasksIds() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type listIdsType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> listIdsReceived = gson.fromJson(response.body(), listIdsType);

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertNotNull(listIdsReceived, "Список сабтасков эпика не получен");
        assertEquals(epics.get(5).getSubTasksIds(), listIdsReceived, "Получен неверный список сабтасков эпика");
    }

    @Test
    void shouldGetHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type historyType = new TypeToken<List<Integer>>() {
        }.getType();
        List<Integer> history = gson.fromJson(response.body(), historyType);

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertNotNull(history, "История вызова не получена");
        assertEquals(taskManager.getHistoryIds(), history, "Получена неверная история вызова задач");
    }

    @Test
    void shouldGetPrioritizedTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type prioritizedTasksIdsType = new TypeToken<List<Integer>>() {
        }.getType();
        List<Integer> prioritizedTasks = gson.fromJson(response.body(), prioritizedTasksIdsType);

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 200");
        assertNotNull(prioritizedTasks, "Список задач по приоритету не получен");
        assertEquals(taskManager.getPrioritizedTasksIds(), prioritizedTasks, "Получен неверный список задач");
    }

    @Test
    void shouldUnknownRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/mask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом - 400");
        assertEquals("Неизвестный запрос", response.body(), "Неверное тело ответа");
    }

}