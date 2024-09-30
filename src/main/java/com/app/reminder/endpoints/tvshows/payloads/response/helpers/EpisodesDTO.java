package com.app.reminder.endpoints.tvshows.payloads.response.helpers;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class EpisodesDTO {
    private String episodeName;
    private ZonedDateTime episodeDateTime;
}
