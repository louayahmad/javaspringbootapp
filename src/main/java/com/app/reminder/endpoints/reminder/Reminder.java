package com.app.reminder.endpoints.reminder;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.app.reminder.endpoints.episodes.Episode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reminder")
public class Reminder {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Column(name = "reminder_datetime", nullable = false)
    private ZonedDateTime reminderDateTime;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    public Reminder() {
    }

    public Reminder(Episode episode, ZonedDateTime reminderDateTime, String userEmail) {
        this.episode = episode;
        this.reminderDateTime = reminderDateTime;
        this.userEmail = userEmail;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    public ZonedDateTime getReminderDateTime() {
        return reminderDateTime;
    }

    public void setReminderDateTime(ZonedDateTime reminderDateTime) {
        this.reminderDateTime = reminderDateTime;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
