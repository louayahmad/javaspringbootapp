package com.app.reminder.endpoints.episodes.payloads.request;

import lombok.Data;

@Data
public class EpisodeRequest {
    private String tvShowId;
    private String tvShowName;
}
