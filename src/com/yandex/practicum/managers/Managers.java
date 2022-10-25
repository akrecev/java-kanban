package com.yandex.practicum.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.practicum.adapters.LocalDateTimeAdapter;
import com.yandex.practicum.server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefault() {
        return new HttpTaskManager(KVServer.PORT);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static KVServer getDefaultKVServer() throws IOException {
        return new KVServer();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
}
