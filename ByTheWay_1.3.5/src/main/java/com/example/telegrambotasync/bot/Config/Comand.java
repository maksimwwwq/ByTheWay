package com.example.telegrambotasync.bot.Config;

import java.util.ArrayList;

public class Comand {
    public String[][] СheckCharacteristic(String CharacteristicsCheck, ArrayList<String> deckription, String[][] CharacteristicsKeyboard) {
        if (deckription.contains(CharacteristicsCheck)) {
            deckription.remove(CharacteristicsCheck);
            int rowIndex = -1;
            int columnIndex = -1;
            outerLoop:
            for (int i = 0; i < CharacteristicsKeyboard.length; i++) {
                for (int j = 0; j < CharacteristicsKeyboard[i].length; j++) {
                    if (CharacteristicsKeyboard[i][j].equals("✅" + CharacteristicsCheck)) {
                        rowIndex = i;
                        columnIndex = j;
                        break outerLoop; // Элемент найден, выходим из обоих циклов
                    }
                }
            }
            CharacteristicsKeyboard[rowIndex][columnIndex] = CharacteristicsCheck;
        } else {
            deckription.add(CharacteristicsCheck);
            int rowIndex = -1;
            int columnIndex = -1;
            outerLoop:
            for (int i = 0; i < CharacteristicsKeyboard.length; i++) {
                for (int j = 0; j < CharacteristicsKeyboard[i].length; j++) {
                    if (CharacteristicsKeyboard[i][j].equals(CharacteristicsCheck)) {
                        rowIndex = i;
                        columnIndex = j;
                        break outerLoop; // Элемент найден, выходим из обоих циклов
                    }
                }
            }
            CharacteristicsKeyboard[rowIndex][columnIndex] = "✅" + CharacteristicsCheck;
        }
        return CharacteristicsKeyboard;
    }
    public String[][] Сhecklokation(String loketionCheck, ArrayList<String> deckription, String[][] loketionKeyboard) {
        if (deckription.contains(loketionCheck)) {
            deckription.remove(loketionCheck);
            int rowIndex = -1;
            int columnIndex = -1;
            outerLoop:
            for (int i = 0; i < loketionKeyboard.length; i++) {
                for (int j = 0; j < loketionKeyboard[i].length; j++) {
                    if (loketionKeyboard[i][j].equals("✅" + loketionCheck)) {
                        rowIndex = i;
                        columnIndex = j;
                        break outerLoop; // Элемент найден, выходим из обоих циклов
                    }
                }
            }
            loketionKeyboard[rowIndex][columnIndex] = loketionCheck;
        } else {
            deckription.add(loketionCheck);
            int rowIndex = -1;
            int columnIndex = -1;
            outerLoop:
            for (int i = 0; i < loketionKeyboard.length; i++) {
                for (int j = 0; j < loketionKeyboard[i].length; j++) {
                    if (loketionKeyboard[i][j].equals(loketionCheck)) {
                        rowIndex = i;
                        columnIndex = j;
                        break outerLoop; // Элемент найден, выходим из обоих циклов
                    }
                }
            }
            loketionKeyboard[rowIndex][columnIndex] = "✅" + loketionCheck;
        }
        return loketionKeyboard;
    }
}
