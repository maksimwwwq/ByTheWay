package com.example.telegrambotasync.util;

import com.example.telegrambotasync.bot.Config.UserData;
//import com.example.telegrambotasync.bot.TeleBot;
import com.example.telegrambotasync.bot.TeleBot;
import com.example.telegrambotasync.bot.processing_request.Independent_routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Component
public class Keyboards {
//    private TeleBot teleBot;
//
//    @Autowired
//    public void setTeleBot(TeleBot teleBot) {
//        this.teleBot = teleBot;
//    }

    public InlineKeyboardMarkup inlineKeyboardButtonCity() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        String[][] cities = {
                {"Минск", "Минская обл."},
                {"Брестская обл.","Гомельская обл."},
                {"Могилёвская обл.", "Гродненская обл."},
                {"Витебская обл.", "Выбрать все"},
                {"⬅\uFE0FНазад","Подобрать⌛\uFE0F"}
        };

        for (String[] cityRow : cities) {
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            Arrays.stream(cityRow)
                    .map(city -> {
                        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                        inlineKeyboardButton.setText(city);
                        inlineKeyboardButton.setCallbackData(city);
                        return inlineKeyboardButton;
                    })
                    .forEach(keyboardButtonsRow::add);
            rowList.add(keyboardButtonsRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
    public InlineKeyboardMarkup inlineKeyboardButtonLokationUpdate(String[][] CharacteristicsKeyboard) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (String[] row : CharacteristicsKeyboard) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();

            for (String buttonText : row) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(buttonText);
                button.setCallbackData(buttonText);
                buttonRow.add(button);
            }

            rowList.add(buttonRow);
        }
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("⬅\uFE0FНазад");
        button.setCallbackData("⬅\uFE0FНазад");
        buttonRow.add(button);
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Подобрать⌛\uFE0F");
        button1.setCallbackData("Подобрать⌛\uFE0F");
        buttonRow.add(button1);
        rowList.add(buttonRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
    public InlineKeyboardMarkup inlineKeyboardButtonCharacteristicsUpdate(String[][] CharacteristicsKeyboard) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (String[] row : CharacteristicsKeyboard) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();

            for (String buttonText : row) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(buttonText);
                button.setCallbackData(buttonText);
                buttonRow.add(button);
            }

            rowList.add(buttonRow);
        }
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Далее➡\uFE0F");
        button.setCallbackData("Далее");
        buttonRow.add(button);
        rowList.add(buttonRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
    public void inlineKeyboardButtonCharacteristics(SendMessage sendMessage) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        String[][] cities = {
                {"История", "Природа"},
                {"С детьми","Спорт"},
                {"Архитектура", "Гастрономия"},
                {"Искусство", "Фотография"},
                {"Животные","Религия"},
                {"Фестивали", "Экстрим"},
        };

        for (String[] cityRow : cities) {
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            Arrays.stream(cityRow)
                    .map(city -> {
                        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                        inlineKeyboardButton.setText(city);
                        inlineKeyboardButton.setCallbackData(city);
                        return inlineKeyboardButton;
                    })
                    .forEach(keyboardButtonsRow::add);
            rowList.add(keyboardButtonsRow);
        }
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Далее➡\uFE0F");
        button.setCallbackData("Далее");
        buttonRow.add(button);
        rowList.add(buttonRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }
    public void replyKeyboardMarkupbasic(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboardRowArrayList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("\uD83D\uDE98Самостоятельные  маршруты"));
        KeyboardRow keyboardRow1 = new KeyboardRow();
  //      keyboardRow1.add(new KeyboardButton("\uD83C\uDF9FОрганизованные туры"));
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("\uD83C\uDF1FИзбранные"));
        keyboardRow2.add(new KeyboardButton("\uD83D\uDCF2Связь"));
        keyboardRowArrayList.add(keyboardRow);
        //keyboardRowArrayList.add(keyboardRow1);
        keyboardRowArrayList.add(keyboardRow2);
        replyKeyboardMarkup.setKeyboard(keyboardRowArrayList);
        replyKeyboardMarkup.setResizeKeyboard(true);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
    public void replyKeyboardMarkupRoutes(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboardRowArrayList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("⬅\uFE0F"));
        keyboardRow.add(new KeyboardButton("\uD83D\uDCA4"));
        keyboardRow.add(new KeyboardButton("➡\uFE0F"));
        keyboardRowArrayList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowArrayList);
        replyKeyboardMarkup.setResizeKeyboard(true);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
}
