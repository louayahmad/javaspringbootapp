package com.app.reminder.genres;

import java.util.UUID;

import com.app.reminder.tvshows.TVShow;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "genres")
public class Genre {
    
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "genre", nullable = false)
    private String genre;

    @ManyToOne
    @JoinColumn(name = "tv_show_id")
    private TVShow tvShow;

    public Genre() {}

    public Genre(String genre, TVShow tvShow){
        this.genre = genre;
        this.tvShow = tvShow;
    }

    public String getGenre(){
        return genre;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }
}
