package com.app.reminder.movie;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.reminder.services.HttpRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private HttpRequest httpRequest;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/add")
    public ResponseEntity<String> addMovie(@RequestBody Movie movie) {
        movieRepository.save(movie);
        return new ResponseEntity<>("Movie " + movie.getTitle() + " added successfully!", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> retrieveMovies() {
        String response = httpRequest.get(
                "https://api.themoviedb.org/3/movie/popular",
                "text"
        );
    
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode resultsNode = rootNode.path("results");
            List<Movie> movies = new ArrayList<>();
    
            Iterator<JsonNode> elements = resultsNode.elements();
            while (elements.hasNext()) {
                JsonNode movieNode = elements.next();
            
                String title = movieNode.path("title").asText();
                String year = movieNode.path("release_date").asText().split("-")[0]; // Extract year from release_date as String
                String rated = movieNode.path("original_language").asText(); // Assuming `original_language` as a placeholder for `rated`
                String released = movieNode.path("release_date").asText();
                String poster = "https://image.tmdb.org/t/p/w500" + movieNode.path("poster_path").asText();
            
                Movie movie = new Movie(title, year, rated, released, poster);
                movies.add(movie);
            }
            
    
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }
    
}
