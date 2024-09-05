package com.app.reminder.tvshows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.reminder.episodes.Episode;
import com.app.reminder.episodes.EpisodeRepository;
import com.app.reminder.genres.Genre;
import com.app.reminder.genres.GenreRepository;



@RestController
@RequestMapping("/tv-show")
public class TVShowController {

    @Autowired
    private TVShowRepository tvShowRepository;
    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private GenreRepository genreRepository;

    public TVShowController(TVShowRepository tvShowRepository, EpisodeRepository episodeRepository, GenreRepository genreRepository){
        this.tvShowRepository = tvShowRepository;
        this.episodeRepository = episodeRepository;
        this.genreRepository = genreRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addMovie(@RequestBody TVShow tvshow) {
        tvShowRepository.save(tvshow);
        return new ResponseEntity<>("TVShow " + tvshow.getName() + " added successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TVShow>> allTvShows() {
        TVShow show1 = new TVShow();
        show1.setId("prison_break");
        show1.setName("Prison Break");
        show1.setNetwork("FOX");
        show1.setLanguage("English");
        show1.setPremiered("2005-08-29");
        show1.setEnded("2009-05-15");

        TVShow show2 = new TVShow();
        show2.setId("breaking_bad");
        show2.setName("Breaking Bad");
        show2.setNetwork("AMC");
        show2.setLanguage("English");
        show2.setPremiered("2008-01-20");
        show2.setEnded("2013-09-29");

        List<TVShow> shows = new ArrayList<>();
        shows.add(show1);
        shows.add(show2);

        tvShowRepository.saveAll(shows);

        Genre genre1 = new Genre("Drama", show1);
        Genre genre2 = new Genre("Thriller", show1);

        Genre genre3 = new Genre("Crime", show2);
        Genre genre4 = new Genre("Drama", show2);
        Genre genre5 = new Genre("Thriller", show2);

        List<Genre> genres = new ArrayList<>();
        genres.add(genre1);
        genres.add(genre2);
        genres.add(genre3);
        genres.add(genre4);
        genres.add(genre5);

        genreRepository.saveAll(genres);

        Episode episode1 = new Episode("prison_break_pilot","Pilot", LocalDateTime.of(2005, 8, 29, 20, 0), show1);
        Episode episode2 = new Episode("prison_break_allen","Allen", LocalDateTime.of(2005, 9, 5, 20, 0), show1);
    
        Episode episode3 = new Episode("breaking_bad_pilot", "Pilot", LocalDateTime.of(2008, 1, 20, 21, 0), show2);
    
        episodeRepository.saveAll(List.of(episode1, episode2, episode3));

        List<TVShow> data = tvShowRepository.findAll();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
}
