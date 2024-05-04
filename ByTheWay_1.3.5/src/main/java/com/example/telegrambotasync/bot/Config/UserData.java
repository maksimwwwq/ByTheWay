package com.example.telegrambotasync.bot.Config;

import java.util.ArrayList;

public class UserData {
    private  String[][] loketionKeyboard;
    private long id; // ID пользователя
    private String[][] characteristicsKeyboard; // Клавиатура для выбора характеристик
    private ArrayList<String> deckription; // Список интересов пользователя
    private String characteristicsCheck; // Выбранная характеристика
    private String loketionCheck; // Выбранная локация

    // Конструктор класса
    public UserData(long id) {
        this.id = id;
        this.characteristicsKeyboard = new String[][]{}; // Инициализируйте с пустым массивом, если нужно
        this.deckription = new ArrayList<>();
        this.characteristicsCheck = "";
        this.loketionCheck = "";
        this.loketionKeyboard = new String[][]{}; // Инициализируйте с пустым массивом, если нужно
    }

    // Геттеры и сеттеры
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String[][] getLoketionKeyboard() {
        return loketionKeyboard;
    }

    public void setLoketionKeyboard(String[][] characteristicsKeyboard) {
        this.loketionKeyboard = characteristicsKeyboard;
    }

    public String[][] getCharacteristicsKeyboard() {
        return characteristicsKeyboard;
    }

    public void setCharacteristicsKeyboard(String[][] characteristicsKeyboard) {
        this.characteristicsKeyboard = characteristicsKeyboard;
    }

    public ArrayList<String> getDeckription() {
        return deckription;
    }

    public void setDeckription(ArrayList<String> deckription) {
        this.deckription = deckription;
    }

    public String getCharacteristicsCheck() {
        return characteristicsCheck;
    }

    public void setCharacteristicsCheck(String characteristicsCheck) {
        this.characteristicsCheck = characteristicsCheck;
    }

    public String getLoketionCheck() {
        return loketionCheck;
    }

    public void setLoketionCheck(String loketionCheck) {
        this.loketionCheck = loketionCheck;
    }

    // Добавьте другие методы и поля, если нужно
}
