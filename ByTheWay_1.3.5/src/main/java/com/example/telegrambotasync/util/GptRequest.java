package com.example.telegrambotasync.util;

import org.asynchttpclient.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class GptRequest {
    String apiKey = "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";
    String model = "gpt-3.5-turbo";


    @Async("asyncExecutor")
    public CompletableFuture<String> callGptApi(String inputText) {
        System.out.println(Thread.currentThread().getName() + " выполняет запрос");

        String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + inputText + "\"}]}";

        try(AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient()) {
            // Подготовка запроса к GPT API
            String url = "https://api.openai.com/v1/chat/completions";
            Request request = new RequestBuilder()
                    .setUrl(url)
                    .setMethod("POST")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .setBody(body)
                    .build();

            // Отправка асинхронного запроса к GPT API
            Response response = asyncHttpClient.prepareRequest(request).execute().get();

            // Обработка ответа
            if (response.getStatusCode() == 200) {
                return CompletableFuture.completedFuture(response.getResponseBody());
            } else {
                throw new RuntimeException("Ошибка при вызове GPT API: " + response.getStatusText());
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при вызове GPT API", e);
        }
    }

}
