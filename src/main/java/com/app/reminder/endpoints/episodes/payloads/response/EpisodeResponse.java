package com.app.reminder.endpoints.episodes.payloads.response;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class EpisodeResponse {
    private String episodeId;
    private String episodeName;
    private ZonedDateTime episodeAirDateTime;
}
