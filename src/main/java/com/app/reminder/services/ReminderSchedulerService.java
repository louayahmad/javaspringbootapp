package com.app.reminder.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.reminder.components.ReminderJob;
import com.app.reminder.episodes.Episode;

@Service
public class ReminderSchedulerService {

    @Autowired
    private Scheduler scheduler;

    public void scheduleReminderJob(String email, String showName, LocalDateTime episodeAirDate) throws SchedulerException {
        LocalDateTime reminderTime = episodeAirDate.minusMinutes(2);

        JobDetail jobDetail = JobBuilder.newJob(ReminderJob.class)
                .withIdentity(showName + "ReminderJob", "episodeReminderGroup")
                .usingJobData("email", email)
                .usingJobData("showName", showName)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(showName + "ReminderTrigger", "episodeReminderGroup")
                .startAt(Date.from(reminderTime.atZone(ZoneId.systemDefault()).toInstant())) 
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }


    public void scheduleEpisodeReminder(String email, Episode episode) {
        try {
            String userEmail = email;  

            scheduleReminderJob(userEmail, episode.getTvShow().getName(), episode.getDatetime());
        } catch (SchedulerException e) {
            e.printStackTrace(); 
        }
    }
}
