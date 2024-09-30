package com.app.reminder.endpoints.tvshows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.app.reminder.endpoints.tvshows.payloads.apis.TVMazeAPIShows;
import com.app.reminder.endpoints.tvshows.payloads.response.TVShowsResponse;
import com.app.reminder.utils.services.HttpRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping
@Tag(name = "TV Shows", description = "Get currently airing TV shows.")
public class TVShowController {

    private static final Logger logger = LoggerFactory.getLogger(TVShowController.class);

    @Autowired
    private HttpRequest httpRequest;

    @Autowired
    private TVShowRepository tvShowRepository; 

    private static final String TVMAZE_API_BASE_URL = "https://api.tvmaze.com";
    private static final String API_KEY = "";

    @GetMapping("/tvshows")
    public ResponseEntity<List<TVShowsResponse>> getAllTvShows() {
        logger.info("Fetching all currently airing TV shows from TV Maze API");
    
        String tvMazeAPIUrl = TVMAZE_API_BASE_URL + "/show";
        ResponseEntity<String> response = httpRequest.get(tvMazeAPIUrl, null, API_KEY, null);
    
        if (response.getStatusCode() != HttpStatus.OK) {
            logger.error("TV Maze API call failed with status code: {}", response.getStatusCode());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "TV Maze API failed.");
        }
    
        String tvMazeAPIShows = response.getBody();
        if (tvMazeAPIShows == null) {
            logger.error("Received empty response from TV Maze API.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Empty response from TV Maze API.");
        }
    
        ObjectMapper objectMapper = new ObjectMapper();
        List<TVShowsResponse> allShows = new ArrayList<>();
        List<TVShow> newShows = new ArrayList<>();
    
        try {
            List<TVMazeAPIShows> showsArray = objectMapper.readValue(tvMazeAPIShows, new TypeReference<List<TVMazeAPIShows>>() {});
            
            Set<String> existingShowIds = new HashSet<>();
            for (TVShow existingShow : tvShowRepository.findAll()) {
                existingShowIds.add(existingShow.getId());
            }
    
            for (TVMazeAPIShows apiShow : showsArray) {
                if (allShows.size() == 20){
                    break;
                }

                if (apiShow.getStatus() != null && "Running".equals(apiShow.getStatus()) &&
                    apiShow.getNetwork() != null && apiShow.getNetwork().getCountry() != null &&
                    "US".equals(apiShow.getNetwork().getCountry().getCode())) {
                    
                    String showId = apiShow.getName().toLowerCase().replace(" ", "_");
                    String showName = apiShow.getName();
                    String showLanguage = apiShow.getLanguage();
                    String showPremiered = apiShow.getPremiered();
                    String showStatus = apiShow.getStatus();
                    String showNetwork = apiShow.getNetwork().getName();

                    TVShow tvShow = new TVShow(showId, showName, showLanguage, null, 
                    showPremiered, showStatus, showNetwork, null);

                    TVShowsResponse tvShowsResponse = new TVShowsResponse(showId, showName, showLanguage, 
                    showPremiered, showStatus, showNetwork);
    
                    allShows.add(tvShowsResponse);
                
                    if (!existingShowIds.contains(tvShow.getId())) {
                        newShows.add(tvShow);
                        logger.info("Prepared new TV show for saving: {}", tvShow.getName());
                    } else {
                        logger.info("TV show already exists: {}", tvShow.getName());
                    }
                }
            }
            
            if (!newShows.isEmpty()) {
                tvShowRepository.saveAll(newShows);
                logger.info("Saved {} new TV shows.", newShows.size());
            }
    
            logger.info("Successfully fetched and processed {} currently airing shows.", allShows.size());
            return new ResponseEntity<>(allShows, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Error parsing response from TV Maze API: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing TV shows data.");
        }
    }
}