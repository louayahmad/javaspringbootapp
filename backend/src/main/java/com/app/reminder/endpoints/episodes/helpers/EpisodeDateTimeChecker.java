package com.app.reminder.endpoints.episodes.helpers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class EpisodeDateTimeChecker {
    private static final ZoneId NEW_YORK_ZONE = ZoneId.of("America/New_York");

    public static Map<Boolean, ZonedDateTime> isEpisodeInFuture(String airDate, String airTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate date = LocalDate.parse(airDate, dateFormatter);
        LocalTime time = LocalTime.parse(airTime, timeFormatter);

        ZonedDateTime episodeDateTime = ZonedDateTime.of(date.atTime(time), NEW_YORK_ZONE);

        ZonedDateTime now = ZonedDateTime.now(NEW_YORK_ZONE);

        Map<Boolean, ZonedDateTime> episodeDateTimeInfo = new HashMap<>();
        episodeDateTimeInfo.put(episodeDateTime.isAfter(now), episodeDateTime);

        return episodeDateTimeInfo;
    }
}
