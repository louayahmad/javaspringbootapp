package com.app.reminder.tvshows;

import java.util.List;

import com.app.reminder.episodes.Episode;
import com.app.reminder.genres.Genre;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tvshows")
public class TVShow {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id; 
    
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "language")
    private String language;

    @Column(name = "premiered")
    private String premiered;

    @Column(name = "ended")
    private String ended;

    @Column(name = "network")
    private String network;

    @OneToMany(mappedBy = "tvShow")
    private List<Genre> genres;

    @OneToMany(mappedBy = "tvShow")
    private List<Episode> episodes;

    public TVShow(){}

    public TVShow(String id, String name, String language, List<Genre> genres, String premiered, 
                  String ended, String network, List<Episode> episodes) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.genres = genres;
        this.premiered = premiered;
        this.ended = ended;
        this.network = network;
        this.episodes = episodes;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getPremiered() {
        return premiered;
    }

    public void setPremiered(String premiered) {
        this.premiered = premiered;
    }

    public String getEnded() {
        return ended;
    }

    public void setEnded(String ended) {
        this.ended = ended;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}
