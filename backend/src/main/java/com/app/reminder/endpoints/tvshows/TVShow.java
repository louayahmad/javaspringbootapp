package com.app.reminder.endpoints.tvshows;

import java.util.List;

import com.app.reminder.endpoints.episodes.Episode;

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

    @Column(name = "tvMazeShowId", updatable = false, nullable = false)
    private Integer tvMazeShowId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "language")
    private String language;

    @Column(name = "premiered")
    private String premiered;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "network")
    private String network;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "tvShow")
    private List<Episode> episodes;

    public TVShow() {
    }

    public TVShow(String id, Integer tvMazeShowId, String name, String language, String premiered,
            String status, String network, String image, List<Episode> episodes) {
        this.id = id;
        this.tvMazeShowId = tvMazeShowId;
        this.name = name;
        this.language = language;
        this.premiered = premiered;
        this.status = status;
        this.network = network;
        this.image = image;
        this.episodes = episodes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTVMazeShowId() {
        return tvMazeShowId;
    }

    public void setTVMazeShowId(Integer tvMazeShowId) {
        this.tvMazeShowId = tvMazeShowId;
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

    public String getPremiered() {
        return premiered;
    }

    public void setPremiered(String premiered) {
        this.premiered = premiered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}
