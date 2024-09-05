package com.app.reminder.tvshows.payloads.response.helpers;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EpisodesDTO {
    private String episodeName;
    private LocalDateTime episodeDateTime;
}
