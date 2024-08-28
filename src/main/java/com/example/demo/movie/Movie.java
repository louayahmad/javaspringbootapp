package com.example.demo.movie;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;  

    @Column(name = "title")
    private String title;

    @Column(name = "year")
    private String year;

    @Column(name = "rated")
    private String rated;

    @Column(name = "released")
    private String released;

    @Column(name = "poster")
    private String poster;

    // Default constructor
    public Movie() {}

    // Parameterized constructor
    public Movie(String title, String year, String rated, String released, String poster) {
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.poster = poster;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
