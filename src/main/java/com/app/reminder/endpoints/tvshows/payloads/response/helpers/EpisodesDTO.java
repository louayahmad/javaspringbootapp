package com.app.reminder.endpoints.tvshows.payloads.response.helpers;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EpisodesDTO {
    private String episodeName;
    private LocalDateTime episodeDateTime;
}
