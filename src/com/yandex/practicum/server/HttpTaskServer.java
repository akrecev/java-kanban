package com.yandex.practicum.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.yandex.practicum.managers.Managers;
import com.yandex.practicum.managers.TaskManager;
import com.yandex.practicum.tasks.Epic;
import com.yandex.practicum.tasks.Subtask;
import com.yandex.practicum.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private final HttpServer httpServer;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.gson = Managers.getGson();
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту.");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер на порту " + PORT + " остановлен.");
    }

    class TasksHandler implements HttpHandler {
        private String method;
        private String query;
        private String path;
        private final Charset CHARSET = StandardCharsets.UTF_8;

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            method = httpExchange.getRequestMethod();
            path = httpExchange.getRequestURI().getPath();
            query = httpExchange.getRequestURI().getQuery();
            String pathShort = httpExchange.getRequestURI().getPath().replaceFirst("/tasks/", "");
            InputStream bodyStream = httpExchange.getRequestBody();
            String bodyString = new String(bodyStream.readAllBytes(), CHARSET);

            String response = "";
            int statusCode = 200;

            switch (pathShort) {

                case "task/":
                    if (method.equals("GET") && query == null) {
                        Map<Integer, Task> tasks = taskManager.getTasks();
                        response = gson.toJson(tasks);
                        printRequest();
                    } else if (method.equals("GET")) {
                        String idString = httpExchange.getRequestURI().getQuery().replaceFirst("id=", "");
                        int id = Integer.parseInt(idString);
                        if (taskManager.getTasks().containsValue(taskManager.getTaskById(id))) {
                            Task task = taskManager.getTaskById(id);
                            response = gson.toJson(task);
                        } else {
                            statusCode = 404;
                            response = "Задача с id:" + id + " отсутствует";
                        }
                        printRequest();
                    } else if (method.equals("POST")) {
                        Task taskFromJson = gson.fromJson(bodyString, Task.class);
                        int id = taskFromJson.getId();
                        if (taskManager.getTasks().containsKey(id)) {
                            taskManager.updateTask(taskFromJson);
                            if (taskManager.getTasks().containsValue(taskFromJson)) {
                                response = "Задача успешно обновлена";
                                statusCode = 202;
                            }
                        } else {
                            taskManager.addTask(taskFromJson);
                            if (taskManager.getTasks().containsValue(taskFromJson)) {
                                response = "Задача успешно добавлена, присвоен id:" + taskFromJson.getId();
                                statusCode = 201;
                            }
                        }
                        printRequest();
                    }
                    if (method.equals("DELETE") && query == null) {
                        taskManager.delAllTasks();
                        if (taskManager.getTasks().isEmpty()) {
                            response = "Все задачи удалены";
                        }
                        printRequest();
                    } else if (method.equals("DELETE")) {
                        String idString = httpExchange.getRequestURI().getQuery().replaceFirst("id=", "");
                        int id = Integer.parseInt(idString);
                        if (taskManager.getTasks().containsValue(taskManager.getTaskById(id))) {
                            taskManager.deleteTaskById(id);
                            if (!taskManager.getTasks().containsValue(taskManager.getTaskById(id))) {
                                response = "Задача id:" + id + " удалена";
                            }
                        } else {
                            statusCode = 404;
                            response = "Задача id:" + id + " отсутствует";
                        }
                        printRequest();
                    }
                    break;

                case "subtask/":
                    if (method.equals("GET") && query == null) {
                        Map<Integer, Subtask> subtasks = taskManager.getSubtasks();
                        response = gson.toJson(subtasks);
                        printRequest();
                    } else if (method.equals("GET")) {
                        String idString = httpExchange.getRequestURI().getQuery().replaceFirst("id=", "");
                        int id = Integer.parseInt(idString);
                        if (taskManager.getSubtasks().containsValue(taskManager.getSubtaskById(id))) {
                            Subtask subtask = taskManager.getSubtaskById(id);
                            response = gson.toJson(subtask);
                        } else {
                            statusCode = 404;
                            response = "Подзадача с id:" + id + " отсутствует";
                        }
                        printRequest();
                    } else if (method.equals("POST")) {
                        Subtask subtaskFromJson = gson.fromJson(bodyString, Subtask.class);
                        int id = subtaskFromJson.getId();
                        if (taskManager.getSubtasks().containsKey(id)) {
                            taskManager.updateSubtask(subtaskFromJson);
                            if (taskManager.getSubtasks().containsValue(subtaskFromJson)) {
                                response = "Подзадача успешно обновлена";
                                statusCode = 202;
                            }
                        } else {
                            taskManager.addSubtask(subtaskFromJson);
                            if (taskManager.getSubtasks().containsValue(subtaskFromJson)) {
                                response = "Подзадача успешно добавлена, присвоен id:" + subtaskFromJson.getId();
                                statusCode = 201;
                            }
                        }
                        printRequest();
                    }
                    if (method.equals("DELETE") && query == null) {
                        taskManager.delAllSubtask();
                        if (taskManager.getSubtasks().isEmpty()) {
                            response = "Все подзадачи удалены";
                        }
                        printRequest();
                    } else if (method.equals("DELETE")) {
                        String idString = httpExchange.getRequestURI().getQuery().replaceFirst("id=", "");
                        int id = Integer.parseInt(idString);
                        if (taskManager.getSubtasks().containsValue(taskManager.getSubtaskById(id))) {
                            taskManager.deleteSubtaskById(id);
                            if (!taskManager.getSubtasks().containsValue(taskManager.getSubtaskById(id))) {
                                response = "Подзадача id:" + id + " удалена";
                            }
                        } else {
                            statusCode = 404;
                            response = "Подзадача id:" + id + " отсутствует";
                        }
                        printRequest();
                    }
                    break;

                case "epic/":
                    if (method.equals("GET") && query == null) {
                        Map<Integer, Epic> epics = taskManager.getEpics();
                        response = gson.toJson(epics);
                        printRequest();
                    } else if (method.equals("GET")) {
                        String idString = httpExchange.getRequestURI().getQuery().replaceFirst("id=", "");
                        int id = Integer.parseInt(idString);
                        if (taskManager.getEpics().containsValue(taskManager.getEpicById(id))) {
                            Epic epic = taskManager.getEpicById(id);
                            response = gson.toJson(epic);
                        } else {
                            statusCode = 404;
                            response = "Эпик с id:" + id + " отсутствует";
                        }
                        printRequest();
                    } else if (method.equals("POST")) {
                        Epic epicFromJson = gson.fromJson(bodyString, Epic.class);
                        int id = epicFromJson.getId();
                        if (taskManager.getEpics().containsKey(id)) {
                            taskManager.updateEpic(epicFromJson);
                            if (taskManager.getEpics().containsValue(epicFromJson)) {
                                response = "Эпик успешно обновлен";
                                statusCode = 202;
                            }
                        } else {
                            taskManager.addEpic(epicFromJson);
                            if (taskManager.getEpics().containsValue(epicFromJson)) {
                                response = "Эпик успешно добавлен, присвоен id:" + epicFromJson.getId();
                                statusCode = 201;
                            }
                        }
                        printRequest();
                    }
                    if (method.equals("DELETE") && query == null) {
                        taskManager.delAllEpics();
                        if (taskManager.getEpics().isEmpty()) {
                            response = "Все эпики удалены";
                        }
                        printRequest();
                    } else if (method.equals("DELETE")) {
                        String idString = httpExchange.getRequestURI().getQuery().replaceFirst("id=", "");
                        int id = Integer.parseInt(idString);
                        if (taskManager.getEpics().containsValue(taskManager.getEpicById(id))) {
                            taskManager.deleteEpicById(id);
                            if (!taskManager.getEpics().containsValue(taskManager.getEpicById(id))) {
                                response = "Эпик id:" + id + " удален";
                            }
                        } else {
                            statusCode = 404;
                            response = "Эпик id:" + id + " отсутствует";
                        }
                        printRequest();
                    }
                    break;

                case "subtask/epic/":
                    if (method.equals("GET") && query != null) {
                        String idString = httpExchange.getRequestURI().getQuery().replaceFirst("id=", "");
                        int id = Integer.parseInt(idString);
                        response = gson.toJson(taskManager.getEpicById(id).getSubTasksIds());
                        printRequest();
                    }
                    break;

                case "history":
                    if (method.equals("GET")) {
                        List<Integer> history = taskManager.getHistoryIds();
                        response = gson.toJson(history);
                        printRequest();
                    }
                    break;

                case "":
                    if (method.equals("GET")) {
                        List<Integer> prioritizedTasks = taskManager.getPrioritizedTasksIds();
                        response = gson.toJson(prioritizedTasks);
                        printRequest();
                    }
                    break;

                default:
                    System.out.println("Неизвестный запрос");
                    statusCode = 400;
                    response = "Неизвестный запрос";
            }

            httpExchange.sendResponseHeaders(statusCode, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        private void printRequest() {
            if (query == null) {
                System.out.println("Запрос " + method + " http://localhost:" + PORT + path);
            } else {
                System.out.println("Запрос " + method + " http://localhost:" + PORT + path + "?" + query);
            }
        }
    }
}
