package com.yandex.practicum.managers;

import com.yandex.practicum.adapters.LocalDateTimeAdapter;
import com.yandex.practicum.tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CsvFormatter {
    final static String SEPARATOR = ",";

    // чтение задачи из строки
    public static Task fromString(String value) {
        String[] data = value.split(SEPARATOR);
        switch (data[1]) {
            case "TASK":
                return new Task(TypeTask.getType(data[1]), data[2], data[4], Integer.parseInt(data[0]),
                        Status.valueOf(data[3]), Long.parseLong(data[5]),
                        LocalDateTime.parse(data[6], LocalDateTimeAdapter.formatter));
            case "EPIC":
                return new Epic(TypeTask.getType(data[1]), data[2], data[4], Integer.parseInt(data[0]),
                        Status.valueOf(data[3]));
            case "SUBTASK":
                return new Subtask(TypeTask.getType(data[1]), data[2], data[4], Integer.parseInt(data[0]),
                        Status.valueOf(data[3]), Integer.parseInt(data[7]), Long.parseLong(data[5]),
                        LocalDateTime.parse(data[6], LocalDateTimeAdapter.formatter));
            default:
                return null;
        }
    }

    // чтение истории просмотров из строки
    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] data = value.split(SEPARATOR);
        for (String s : data) {
            history.add(Integer.parseInt(s));
        }
        return history;
    }

    // запись заголовка в строку
    public static String getHeader() {
        return "id,type,name,status,description,duration,startTime,epic";
    }

    // запись задачи типа Task в строку
    public static String toStringTask(Task task) {
        return task.getId() + SEPARATOR + task.getTypeTask() + SEPARATOR
                + task.getTitle() + SEPARATOR + task.getStatus() + SEPARATOR
                + task.getDescription() + SEPARATOR + task.getDuration() + SEPARATOR
                + task.getStartTime().format(LocalDateTimeAdapter.formatter);
    }

    // запись задачи типа Task в строку
    public static String toStringEpic(Epic epic) {
        return epic.getId() + SEPARATOR + epic.getTypeTask() + SEPARATOR
                + epic.getTitle() + SEPARATOR + epic.getStatus() + SEPARATOR
                + epic.getDescription();
    }

    // запись задачи типа Task в строку
    public static String toStringSubtask(Subtask subtask) {
        return subtask.getId() + SEPARATOR + subtask.getTypeTask() + SEPARATOR
                + subtask.getTitle() + SEPARATOR + subtask.getStatus() + SEPARATOR
                + subtask.getDescription() + SEPARATOR + subtask.getDuration() + SEPARATOR
                + subtask.getStartTime().format(LocalDateTimeAdapter.formatter) + SEPARATOR + subtask.getEpicId();
    }

    // запись истории просмотров в строку
    static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(SEPARATOR);
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }
}
