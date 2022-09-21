package com.yandex.practicum.managers;
import com.yandex.practicum.tasks.*;
import java.util.ArrayList;
import java.util.List;

public class CSVFormatter {
    final static String SEPARATOR = ",";

    public CSVFormatter() {
    }

    // чтение задачи из строки
    public static Task fromString(String value) {
        String[] data = value.split(SEPARATOR);
        switch (data[1]) {
            case "TASK":
                return new Task(TypeTask.getType(data[1]), data[2], data[4], Integer.parseInt(data[0]),
                        Status.getStatus(data[3]));
            case "EPIC":
                return new Epic(TypeTask.getType(data[1]), data[2], data[4], Integer.parseInt(data[0]),
                        Status.getStatus(data[3]));
            case "SUBTASK":
                return new Subtask(TypeTask.getType(data[1]), data[2], data[4], Integer.parseInt(data[0]),
                        Status.getStatus(data[3]), Integer.parseInt(data[5]));
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
        return "id,type,name,status,description,epic";
    }

    // запись задачи типа Task в строку
    public static String toString(Task task) {
        return task.getId() + SEPARATOR + task.getTypeTask() + SEPARATOR
                + task.getTitle() + SEPARATOR + task.getStatus() + SEPARATOR
                + task.getDescription() + SEPARATOR;
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
