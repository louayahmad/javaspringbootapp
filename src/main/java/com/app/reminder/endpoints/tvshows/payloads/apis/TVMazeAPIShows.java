package com.app.reminder.endpoints.tvshows.payloads.apis;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TVMazeAPIShows {
    private int id;
    private String url;
    private String name;
    private String type;
    private String language;
    private List<String> genres;
    private String status;
    private int runtime;
    private int averageRuntime;
    private String premiered;
    private String ended;
    private String officialSite;
    private Schedule schedule;
    private Rating rating;
    private int weight;
    private Network network;
    private Object webChannel;
    private Object dvdCountry;
    private Image image;
    private String summary;
    private long updated;

    @Data
    public static class Schedule {
        private String time;
        private List<String> days;
    }

    @Data
    public static class Rating {
        private double average;
    }

    @Data
    public static class Network {
        private int id;
        private String name;
        private Country country;
        private String officialSite;

        @Data
        public static class Country {
            private String name;
            private String code;
            private String timezone;
        }
    }

    @Data
    public static class Image {
        private String medium;
        private String original;
    }
}