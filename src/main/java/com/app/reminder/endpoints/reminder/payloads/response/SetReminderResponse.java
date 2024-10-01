package com.app.reminder.endpoints.reminder.payloads.response;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetReminderResponse {
    String reminderId;
    String episodeId;
    ZonedDateTime reminderScheduled;
    String message;
}
