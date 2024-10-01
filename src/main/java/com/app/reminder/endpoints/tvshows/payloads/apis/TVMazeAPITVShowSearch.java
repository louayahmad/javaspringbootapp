package com.app.reminder.endpoints.tvshows.payloads.apis;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TVMazeAPITVShowSearch {
    private Double score;
    private Show show;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Show {
        private Integer id;
        private String url;
        private String name;
        private String type;
        private String language;
        private List<String> genres;
        private String status;
        private Integer runtime;
        private Integer averageRuntime;
        private String premiered;
        private String ended;
        private String officialSite;
        private Schedule schedule;
        private Rating rating;
        private Integer weight;
        private Network network;
        private WebChannel webChannel;
        private String dvdCountry;
        private Externals externals;
        private Image image;
        private String summary;
        private Links links;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Schedule {
            private String time;
            private List<String> days;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Rating {
            private Double average;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Network {
            private Integer id;
            private String name;
            private Country country;
            private String officialSite;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Country {
                private String name;
                private String code;
                private String timezone;
            }
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class WebChannel {
            private Integer id;
            private String name;
            private Country country;
            private String officialSite;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Country {
                private String name;
                private String code;
                private String timezone;
            }
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Externals {
            private String tvrage;
            private Integer thetvdb;
            private String imdb;
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
            private PreviousEpisode previousEpisode;
            private NextEpisode nextEpisode;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Self {
                private String href;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class PreviousEpisode {
                private String href;
                private String name;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class NextEpisode {
                private String href;
                private String name;
            }
        }
    }
}
