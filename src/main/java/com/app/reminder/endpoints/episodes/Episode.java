package com.app.reminder.endpoints.episodes;

import java.time.ZonedDateTime;

import com.app.reminder.endpoints.tvshows.TVShow;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "datetime", nullable = false)
    private ZonedDateTime datetime;

    @ManyToOne
    @JoinColumn(name = "tv_show_id")
    private TVShow tvShow;

    public Episode() {
    }

    public Episode(String id, String name, ZonedDateTime datetime, TVShow tvShow) {
        this.id = id;
        this.name = name;
        this.datetime = datetime;
        this.tvShow = tvShow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDatetime() {
        return datetime;
    }

    public void setDateTime(ZonedDateTime datetime) {
        this.datetime = datetime;
    }

    public TVShow getTvShow() {
        return tvShow;
    }

    public void setTvShow(TVShow tvShow) {
        this.tvShow = tvShow;
    }
}