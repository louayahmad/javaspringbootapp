package com.app.reminder.endpoints.episodes.payloads.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TVMazeAPIEpisodes {
    private Integer id;
    private String url;
    private String name;
    private Integer season;
    private Integer number;
    private String type;
    private String airdate;
    private String airtime;
    private String airstamp;
    private Integer runtime;
    private Rating rating;
    private Image image;
    private String summary;
    private Links links;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rating {
        private Double average;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        private String medium;
        private String original;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Links {
        private Self self;
        private Show show;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Self {
            private String href;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Show {
            private String href;
            private String name;
        }
    }
}