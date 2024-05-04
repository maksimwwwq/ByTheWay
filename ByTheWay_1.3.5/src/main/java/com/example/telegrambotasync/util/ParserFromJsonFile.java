package com.example.telegrambotasync.util;

import com.example.telegrambotasync.bot.Config.UserData;
import com.example.telegrambotasync.entity.Routes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ParserFromJsonFile {
    public static ConcurrentHashMap<Long, UserData> userDataMap = new ConcurrentHashMap<>();

    public static List<Routes> getRoutes() throws Exception {
        File jsonFile = new File("json.json");
        ArrayList<Routes> arrayList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);
        Routes routes = null;
        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                String arrCity = String.valueOf(node.get("name"));
                JsonNode descNode = node.get("desc");
                if (descNode.isArray()) {
                    for (JsonNode desc : descNode) {
                        String nane = desc.get("nameRoutes").asText();
                        String[] arrhashtag = desc.get("hashtag").asText().split(",");
                        String Desc = desc.get("description").asText();
                        String Places = desc.get("places").asText();
                        String Url = desc.get("http").asText();
                        routes = Routes.builder().city(arrCity).name(nane).hashtag(arrhashtag).desc(Desc).places(Places).url(Url).build();
                        arrayList.add(routes);
                    }
                }
            }
        }
        return arrayList;
    }





     /*   ArrayList<Routes> list = new ArrayList<>();
        List<Routes> Routes = new ArrayList<>();
        UserData userData = userDataMap.get(id);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File("json.json");
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            for (JsonNode RoutesNode : rootNode) {
                Routes routes = new Routes();
                String routesName = RoutesNode.get("name").asText();
                JsonNode attractions = RoutesNode.get("desc");
                routes.setName(routesName);
                for (JsonNode attraction : attractions) {
                    Iterator<String> iterator = attraction.fieldNames();
                    String fieldName = iterator.next();
                    Attraction attraction1 = new Attraction(attraction.get("nameRoutes").asText(), attraction.get("hashtag").asText(),
                            attraction.get("description").asText(),
                            attraction.get("places").asText(),
                            attraction.get("http").asText());
                    routes.getAttractions().put(attraction1.getName(), attraction1);
                }
                Routes.add(routes);
            }
        } catch (IOException e) {
            System.out.println("Достопримечательность не найдена.");
            throw new RuntimeException(e);
        }
        // Если достопримечательность не найдена
        return Routes;
*/
    }

