package com.app.reminder.endpoints.episodes.payloads.response;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeResponse {
    private String episodeId;
    private String episodeName;
    private ZonedDateTime episodeAirDateTime;
}
