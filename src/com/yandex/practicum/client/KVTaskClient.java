package com.yandex.practicum.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String url;
    private final String apiToken;
    private static final String URN_REGISTER = "/register";
    HttpClient client = HttpClient.newHttpClient();


    public KVTaskClient(int port) {
        url = "http://localhost/" + port;
        apiToken = register(url);
    }

    private String register(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + URN_REGISTER))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else System.out.println("Не удалось получить API_TOKEN. Код ошибки: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, адрес и повторите попытку.");
        }
        return "неверный API_TOKEN";
    }

    public String load(String key) {
        if (apiToken == null) {
            return "API_TOKEN не присвоен";
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/load" + key + "?API_TOKEN=" + apiToken))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return "Ошибка получения запроса";
    }

    public void put(String key, String json)  {
        if (apiToken == null) {
            System.out.println("API_TOKEN не присвоен");
            return;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/save" + key + "?API_TOKEN=" + apiToken))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }
}
