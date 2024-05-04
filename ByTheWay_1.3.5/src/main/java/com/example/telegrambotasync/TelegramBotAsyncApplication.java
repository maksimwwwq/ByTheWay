package com.example.telegrambotasync;

import jakarta.annotation.PostConstruct;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.concurrent.Executor;

@SpringBootApplication
@Configuration
@EnableAsync(proxyTargetClass=true)
public class TelegramBotAsyncApplication extends AsyncConfigurerSupport {

    @Autowired
    @Qualifier("teleBot")  // Добавьте этот qualifier
    private TelegramLongPollingBot teleBot;

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotAsyncApplication.class, args);
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(teleBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {

            System.out.println("Exception: " + ex.getMessage());
            System.out.println("Method Name: " + method.getName());
            ex.printStackTrace();
        };

    }
}
