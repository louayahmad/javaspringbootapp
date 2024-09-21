package com.app.reminder.utils.components;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.reminder.utils.services.SendEmail;

@Component
public class ReminderJob implements Job {

    @Autowired
    private SendEmail sendEmail;

    private static final Logger logger = LoggerFactory.getLogger(ReminderJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            String email = (String) context.getJobDetail().getJobDataMap().get("email");
            String showName = (String) context.getJobDetail().getJobDataMap().get("showName");

            if (email != null && showName != null) {
                sendEmail.sendReminderEmail(email, "TV Show Reminder", "Your show " + showName + " is starting in 30 minutes!");
                logger.info("Reminder email sent to {} for show: {}", email, showName);
            } else {
                logger.error("Email or showName was not provided to the ReminderJob.");
            }
        } catch (Exception e) {
            logger.error("Failed to send reminder email.", e);
            throw new JobExecutionException(e);
        }
    }
}
