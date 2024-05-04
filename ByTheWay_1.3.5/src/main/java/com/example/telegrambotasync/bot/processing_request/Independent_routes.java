package com.example.telegrambotasync.bot.processing_request;

import com.example.telegrambotasync.bot.Config.UserData;
import com.example.telegrambotasync.entity.Routes;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.telegrambotasync.util.ParserFromJsonFile.getRoutes;

public class Independent_routes {

    public static ArrayList<String> SelectionOfIndependentRoutes(ArrayList<String> descPerson) throws Exception {
        ArrayList<String> arrreconciliation = new ArrayList();
        ArrayList<String> arrayList = new ArrayList();
        String CityCheck = "";
        for (int i = 0; i < descPerson.toArray().length; i++) {

            if (descPerson.get(i).equals("Выбрать все")) {
                // Если элемент равен "Выбрать все", добавляем "Минская обл." в список
                descPerson.add("Минская обл.");
                descPerson.add("Минск");
                descPerson.add("Брестская обл.");
                descPerson.add("Гродненская обл.");
                descPerson.add("Гомельская обл.");
                descPerson.add("Витебская обл.");
                descPerson.add("Могилёвская обл.");
            }
        }
        for (Routes routes : getRoutes()) {
            JSONObject jsonObject = new JSONObject();

            for (int i = 0; i < descPerson.toArray().length; i++) {
                if (descPerson.get(i).equals(routes.getCity().substring(1, routes.getCity().length() - 1))) {
                    CityCheck = descPerson.get(i);
                }
            }
            for (int a = 0; a < descPerson.toArray().length; a++) {
                for (int e = 0; e < routes.getHashtag().length; e++) {
                    if (descPerson.get(a).equals(routes.getHashtag()[e]) && CityCheck.equals(routes.getCity().substring(1, routes.getCity().length() - 1))) {
                        if (!arrreconciliation.contains(routes.getName())) {
                            arrreconciliation.add(routes.getName());
                        }
                    }
                }
            }

        } // Пытаетесь получить имя из пустого объекта
        arrayList.add(String.valueOf(arrreconciliation.toArray().length));
        for (Routes routes : getRoutes()) {
            for (int i = 0; i < arrreconciliation.toArray().length; i++) {
                if (arrreconciliation.get(i).equals(routes.getName())) {
                    arrayList.add(routes.getName() + "\n\n" + routes.getDesc() + "\n\n" +"Список мест входящих в маршрут:\n\n" + routes.getPlaces() + "\n\n" + routes.getUrl());
                }

            }
        }
        System.out.println(arrreconciliation);
        return arrayList;
    }

}
