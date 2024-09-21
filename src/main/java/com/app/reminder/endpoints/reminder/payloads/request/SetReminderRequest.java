package com.app.reminder.endpoints.reminder.payloads.request;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SetReminderRequest {
    String episodeId;
    LocalDateTime reminderDateTime;
    String userEmail;
}
