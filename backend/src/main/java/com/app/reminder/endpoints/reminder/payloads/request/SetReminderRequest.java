package com.app.reminder.endpoints.reminder.payloads.request;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class SetReminderRequest {
    private String episodeId;
    private ZonedDateTime reminderDateTime;
    private String userEmail;
}
