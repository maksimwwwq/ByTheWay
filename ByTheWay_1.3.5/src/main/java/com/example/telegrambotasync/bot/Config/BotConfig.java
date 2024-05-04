package com.example.telegrambotasync.bot.Config;

import com.example.telegrambotasync.bot.TeleBot;
import com.example.telegrambotasync.util.GptRequest;
import com.example.telegrambotasync.util.Keyboards;
import com.example.telegrambotasync.util.ParserFromJsonFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

    private final GptRequest gptRequest;
    private final ParserFromJsonFile parserFromJsonFile;
    private final Keyboards keyboards;

    @Autowired
    public BotConfig(GptRequest gptRequest, ParserFromJsonFile parserFromJsonFile, Keyboards keyboards) {
        this.gptRequest = gptRequest;
        this.parserFromJsonFile = parserFromJsonFile;
        this.keyboards = keyboards;
    }

    @Bean
    public TeleBot teleBot() {
        return new TeleBot(gptRequest, parserFromJsonFile, keyboards);
    }
}