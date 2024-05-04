package com.example.telegrambotasync.entity;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@Data
@EqualsAndHashCode
//@AllArgsConstructor
//@NoArgsConstructor
public class Routes {
    private String city;
    private String[] hashtag;
    private String name;
    private String url;
    private String desc;
    private String places;

   /*  Геттеры и сеттеры     для поля City
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // Геттер и сеттер для поля Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }*/

    //    public String getUrl() {
//        return url;
//    }




   /* @Getter
    private  String name;
    private  Map<String, Attraction> attractions = new HashMap<>();
    @Getter
    private  String desc;

    public void setName(String name) {
        this.name = name;
    }

    public  Map<String, Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(Map<String, Attraction> attractions) {
        this.attractions = attractions;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }*/

}
