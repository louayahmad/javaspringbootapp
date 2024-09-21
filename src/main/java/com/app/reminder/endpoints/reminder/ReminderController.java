package com.app.reminder.endpoints.reminder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.app.reminder.endpoints.episodes.Episode;
import com.app.reminder.endpoints.episodes.EpisodeRepository;
import com.app.reminder.endpoints.reminder.payloads.request.SetReminderRequest;
import com.app.reminder.endpoints.reminder.payloads.response.SetReminderResponse;
import com.app.reminder.utils.services.ReminderSchedulerService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/reminder")
@Tag(name = "Reminder", description = "Set TV show reminders.")
public class ReminderController {
    
    @Autowired
    private ReminderRepository reminderRepository;  

    @Autowired
    private EpisodeRepository episodeRepository;
    
    @Autowired
    private ReminderSchedulerService reminderSchedulerService;

    private static final Logger logger = LoggerFactory.getLogger(ReminderController.class);

    @PostMapping("/set")
    public ResponseEntity<SetReminderResponse> setReminder(@RequestBody SetReminderRequest setReminderRequest) {        
        Optional<Episode> optionalEpisode = episodeRepository.findById(setReminderRequest.getEpisodeId());
        LocalDateTime reminderDateTime = setReminderRequest.getReminderDateTime();
        String userEmail = setReminderRequest.getUserEmail();

        ZoneId estZoneId = ZoneId.of("America/New_York"); 

        if (optionalEpisode.isEmpty()) {
            logger.info("No episode found with episode id {}.", setReminderRequest.getEpisodeId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Episode not found");
        }

        ZonedDateTime reminderZonedDateTime = reminderDateTime.atZone(estZoneId);
        ZonedDateTime nowZonedDateTime = ZonedDateTime.now(estZoneId);

        if (reminderZonedDateTime.isBefore(nowZonedDateTime)) {
            logger.warn("Reminder datetime {} cannot be in the past.", reminderZonedDateTime);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reminder datetime cannot be in the past");
        }

        Episode episode = optionalEpisode.get();

        Reminder reminder = new Reminder(episode, reminderDateTime, userEmail);
        reminderRepository.save(reminder);

        try {
            reminderSchedulerService.scheduleReminderJob(reminder);
        } catch (SchedulerException error) {
            logger.info("An error has occured while scheduling the reminder!", error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An error has occured while scheduling the reminder!");

        }

        return new ResponseEntity<>(
            new SetReminderResponse(
                reminder.getId().toString(), 
                reminder.getEpisode().getId().toString(), 
                reminder.getReminderDateTime(), 
                String.format("Successfully set reminder for episode: %s", reminder.getEpisode().getName())
            ), 
            HttpStatus.OK
        );    
    }
}
