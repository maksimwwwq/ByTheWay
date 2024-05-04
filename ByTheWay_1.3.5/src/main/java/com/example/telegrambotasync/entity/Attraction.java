package com.example.telegrambotasync.entity;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
//@AllArgsConstructor
@NoArgsConstructor
public class Attraction {
    private String name;
    private String description;
    private String places;
    private String http;
    private String advertising;

    public Attraction(String hashtag, String description, String places, String http, String advertising) {
    }

    public String getName() {
        return name;
    }
}
