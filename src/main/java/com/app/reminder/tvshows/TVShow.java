package com.app.reminder.tvshows;

import java.util.List;
import java.util.UUID;

import com.app.reminder.episodes.Episode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tvshows")
public class TVShow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id; 
    
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "language")
    private String language;
    
    @ElementCollection
    @CollectionTable(
        name = "genres", 
        joinColumns = @JoinColumn(name = "tv_show_id") 
    )
    @Column(name = "genre") 
    private List<String> genres;

    @Column(name = "premiered")
    private String premiered;

    @Column(name = "ended")
    private String ended;

    @Column(name = "network")
    private String network;

    @OneToMany(mappedBy = "tvShow", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Episode> episodes;

    public TVShow(){}

    public TVShow(String name, String language, List<String> genres, String premiered, 
                  String ended, String network, List<Episode> episodes) {
        this.name = name;
        this.language = language;
        this.genres = genres;
        this.premiered = premiered;
        this.ended = ended;
        this.network = network;
        this.episodes = episodes;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
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
