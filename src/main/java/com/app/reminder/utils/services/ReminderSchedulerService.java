package com.app.reminder.utils.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.reminder.endpoints.reminder.Reminder;
import com.app.reminder.utils.components.ReminderJob;

@Service
public class ReminderSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(ReminderSchedulerService.class);

    @Autowired
    private Scheduler scheduler;

    public void scheduleReminderJob(Reminder reminder) throws SchedulerException {
        LocalDateTime reminderTime = reminder.getReminderDateTime().minusMinutes(30);

        JobDetail jobDetail = JobBuilder.newJob(ReminderJob.class)
                .withIdentity(reminder.getEpisode().getName() + "ReminderJob", "episodeReminderGroup")
                .usingJobData("email", reminder.getUserEmail())
                .usingJobData("showName", reminder.getEpisode().getTvShow().getName())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(reminder.getEpisode().getTvShow().getName() + "ReminderTrigger", "episodeReminderGroup")
                .startAt(Date.from(reminderTime.atZone(ZoneId.systemDefault()).toInstant())) 
                .build();

        logger.info("Scheduling reminder for show: {}, episode: {}, reminder time: {}",
                reminder.getEpisode().getTvShow().getName(),
                reminder.getEpisode().getName(),
                reminderTime);

        scheduler.scheduleJob(jobDetail, trigger);

        logger.info("Reminder scheduled successfully for show: {}, episode: {}",
                reminder.getEpisode().getTvShow().getName(),
                reminder.getEpisode().getName());
    }

    public void scheduleEpisodeReminder(Reminder reminder) {
        try {
            logger.info("Attempting to schedule reminder for episode: {} of show: {}",
                    reminder.getEpisode().getName(),
                    reminder.getEpisode().getTvShow().getName());
            
            scheduleReminderJob(reminder);
        } catch (SchedulerException error) {
            logger.error("Failed to schedule reminder for episode: {} of show: {}",
                    reminder.getEpisode().getName(),
                    reminder.getEpisode().getTvShow().getName(), error);
        }
    }
}
