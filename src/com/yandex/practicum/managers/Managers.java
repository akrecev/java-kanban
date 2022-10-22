package com.yandex.practicum.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.practicum.adapters.LocalDateTimeAdapter;

import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
