package com.app.reminder.utils.services;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

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

        private static final Logger LOGGER = LoggerFactory.getLogger(ReminderSchedulerService.class);

        @Autowired
        private Scheduler scheduler;

        public void scheduleReminderJob(Reminder reminder) throws SchedulerException {
                ZonedDateTime reminderTime = reminder.getReminderDateTime().minusMinutes(30);

                String randomId = UUID.randomUUID().toString();
                String jobIdentity = reminder.getEpisode().getName() + "ReminderJob-" + randomId;
                String triggerIdentity = reminder.getEpisode().getTvShow().getName() + "ReminderTrigger-" + randomId;

                JobDetail jobDetail = JobBuilder.newJob(ReminderJob.class)
                                .withIdentity(jobIdentity, "episodeReminderGroup")
                                .usingJobData("email", reminder.getUserEmail())
                                .usingJobData("showName", reminder.getEpisode().getTvShow().getName())
                                .build();

                Trigger trigger = TriggerBuilder.newTrigger()
                                .withIdentity(triggerIdentity, "episodeReminderGroup")
                                .startAt(Date.from(reminderTime.toInstant()))
                                .build();

                LOGGER.info("Scheduling reminder for show: {}, episode: {}, reminder time: {}",
                                reminder.getEpisode().getTvShow().getName(),
                                reminder.getEpisode().getName(),
                                reminderTime);

                scheduler.scheduleJob(jobDetail, trigger);

                LOGGER.info("Reminder scheduled successfully for show: {}, episode: {}",
                                reminder.getEpisode().getTvShow().getName(),
                                reminder.getEpisode().getName());
        }

        public void scheduleEpisodeReminder(Reminder reminder) {
                try {
                        LOGGER.info("Attempting to schedule reminder for episode: {} of show: {}",
                                        reminder.getEpisode().getName(),
                                        reminder.getEpisode().getTvShow().getName());

                        scheduleReminderJob(reminder);
                } catch (SchedulerException error) {
                        LOGGER.error("Failed to schedule reminder for episode: {} of show: {}",
                                        reminder.getEpisode().getName(),
                                        reminder.getEpisode().getTvShow().getName(), error);
                }
        }
}
