package com.app.reminder.endpoints.reminder.payloads.request;
import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class SetReminderRequest {
    String episodeId;
    ZonedDateTime reminderDateTime;
    String userEmail;
}
