package com.app.reminder.endpoints.tvshows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.app.reminder.endpoints.tvshows.payloads.apis.TVMazeAPIShows;
import com.app.reminder.endpoints.tvshows.payloads.apis.TVMazeAPITVShowSearch;
import com.app.reminder.endpoints.tvshows.payloads.request.TVShowsSearchRequest;
import com.app.reminder.endpoints.tvshows.payloads.response.TVShowsResponse;
import com.app.reminder.utils.services.HttpRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping
@Tag(name = "TV Shows", description = "Get currently airing TV shows.")
public class TVShowController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TVShowController.class);

    private static final String TVMAZE_API_BASE_URL = "https://api.tvmaze.com";
    private static final String API_KEY = "";

    @Autowired
    private HttpRequest httpRequest;
    @Autowired
    private TVShowRepository tvShowRepository;

    /**
     * Fetch all currently airing TV shows from TVMaze API.
     * Returns a list of TV shows limited to 20, filtering by status and network
     * country (US).
     */
    @GetMapping("/tvshows")
    public ResponseEntity<List<TVShowsResponse>> getAllTvShows() {
        LOGGER.info("Fetching all currently airing TV shows from TV Maze API");

        String tvMazeAPIUrl = TVMAZE_API_BASE_URL + "/show";
        ResponseEntity<String> response = httpRequest.get(tvMazeAPIUrl, null, API_KEY, null);

        if (response.getStatusCode() != HttpStatus.OK) {
            LOGGER.error("TV Maze API call failed with status code: {}", response.getStatusCode());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "TV Maze API failed.");
        }

        String tvMazeAPIShows = response.getBody();
        if (tvMazeAPIShows == null) {
            LOGGER.error("Received empty response from TV Maze API.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Empty response from TV Maze API.");
        }

        ObjectMapper objectMapperShows = new ObjectMapper();
        List<TVShowsResponse> allShows = new ArrayList<>();
        List<TVShow> newShows = new ArrayList<>();

        try {
            List<TVMazeAPIShows> showsArray = objectMapperShows.readValue(tvMazeAPIShows,
                    new TypeReference<List<TVMazeAPIShows>>() {
                    });

            Set<String> existingShowIds = new HashSet<>();
            for (TVShow existingShow : tvShowRepository.findAll()) {
                existingShowIds.add(existingShow.getId());
            }

            for (TVMazeAPIShows apiShow : showsArray) {
                // Limit the number of shows processed to 20 for performance reasons (pagination
                // to be added).
                if (allShows.size() == 20) {
                    break;
                }

                // Filter shows that are "Running" and aired in the US.
                if ("Running".equals(apiShow.getStatus()) &&
                        apiShow.getNetwork() != null &&
                        apiShow.getNetwork().getCountry() != null &&
                        "US".equals(apiShow.getNetwork().getCountry().getCode())) {

                    String showId, showName, showLanguage, showPremiered, showStatus, showNetwork;
                    Integer tvMazeShowId;

                    // Construct a unique show ID and extract show details.
                    showId = apiShow.getName().toLowerCase().replace(" ", "_");
                    tvMazeShowId = apiShow.getId();
                    showName = apiShow.getName();
                    showLanguage = apiShow.getLanguage();
                    showPremiered = apiShow.getPremiered();
                    showStatus = apiShow.getStatus();
                    showNetwork = apiShow.getNetwork().getName();

                    TVShow tvShow = new TVShow(showId, tvMazeShowId, showName, showLanguage,
                            showPremiered, showStatus, showNetwork, null);

                    TVShowsResponse tvShowsResponse = new TVShowsResponse(showId, tvMazeShowId, showName, showLanguage,
                            showPremiered, showStatus, showNetwork);

                    allShows.add(tvShowsResponse);

                    // Check if the show is not already in the database and prepare it for saving.
                    if (!existingShowIds.contains(tvShow.getId())) {
                        newShows.add(tvShow);
                        LOGGER.info("Prepared new TV show for saving: {}", tvShow.getName());
                    } else {
                        LOGGER.info("TV show already exists: {}", tvShow.getName());
                    }
                }
            }

            if (!newShows.isEmpty()) {
                tvShowRepository.saveAll(newShows);
                LOGGER.info("Saved {} new TV shows.", newShows.size());
            }

            LOGGER.info("Successfully fetched and processed {} currently airing shows.", allShows.size());
            return new ResponseEntity<>(allShows, HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.error("Error parsing response from TV Maze API: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing TV shows data.");
        }
    }

    @PostMapping("/tvshows/search")
    public ResponseEntity<List<TVShowsResponse>> searchTvShows(@RequestBody TVShowsSearchRequest tvShowSearchRequest) {
        LOGGER.info("Searching for the running TV Shows from TV Maze API.");
        LOGGER.info("Received TVShowSearch object: {}", tvShowSearchRequest);

        String tvMazeAPIUrl = TVMAZE_API_BASE_URL + "/search/shows";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("q", tvShowSearchRequest.getShowName());
        LOGGER.info("{}", queryParams);

        ResponseEntity<String> response = httpRequest.get(tvMazeAPIUrl, null, API_KEY, queryParams);

        if (response.getStatusCode() != HttpStatus.OK) {
            LOGGER.error("TV Maze API call failed with status code: {}", response.getStatusCode());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "TV Maze API failed.");
        }

        String tvMazeAPIShows = response.getBody();
        if (tvMazeAPIShows == null) {
            LOGGER.error("Received empty response from TV Maze API.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Empty response from TV Maze API.");
        }

        ObjectMapper objectMapperShows = new ObjectMapper();
        List<TVShowsResponse> allShows = new ArrayList<>();
        List<TVShow> newShows = new ArrayList<>();

        try {
            List<TVMazeAPITVShowSearch> showsArray = objectMapperShows.readValue(tvMazeAPIShows,
                    new TypeReference<List<TVMazeAPITVShowSearch>>() {
                    });

            Set<String> existingShowIds = new HashSet<>();
            for (TVShow existingShow : tvShowRepository.findAll()) {
                existingShowIds.add(existingShow.getId());
            }

            for (TVMazeAPITVShowSearch apiShow : showsArray) {
                // Filter shows that are "Running" and aired in the US.
                if ("Running".equals(apiShow.getShow().getStatus()) &&
                        apiShow.getShow().getNetwork() != null &&
                        apiShow.getShow().getNetwork().getCountry() != null &&
                        "US".equals(apiShow.getShow().getNetwork().getCountry().getCode())) {

                    String showId, showName, showLanguage, showPremiered, showStatus, showNetwork;
                    Integer tvMazeShowId;

                    // Construct a unique show ID and extract show details.
                    showId = apiShow.getShow().getName().toLowerCase().replace(" ", "_");
                    tvMazeShowId = apiShow.getShow().getId();
                    showName = apiShow.getShow().getName();
                    showLanguage = apiShow.getShow().getLanguage();
                    showPremiered = apiShow.getShow().getPremiered();
                    showStatus = apiShow.getShow().getStatus();
                    showNetwork = apiShow.getShow().getNetwork().getName();

                    TVShow tvShow = new TVShow(showId, tvMazeShowId, showName, showLanguage,
                            showPremiered, showStatus, showNetwork, null);

                    TVShowsResponse tvShowsResponse = new TVShowsResponse(showId, tvMazeShowId, showName, showLanguage,
                            showPremiered, showStatus, showNetwork);

                    allShows.add(tvShowsResponse);

                    // Check if the show is not already in the database and prepare it for saving.
                    if (!existingShowIds.contains(tvShow.getId())) {
                        newShows.add(tvShow);
                        LOGGER.info("Prepared new TV show for saving: {}", tvShow.getName());
                    } else {
                        LOGGER.info("TV show already exists: {}", tvShow.getName());
                    }
                }
            }

            if (!newShows.isEmpty()) {
                tvShowRepository.saveAll(newShows);
                LOGGER.info("Saved {} new TV shows.", newShows.size());
            }

            LOGGER.info("Successfully fetched and processed {} currently airing shows.", allShows.size());
            return new ResponseEntity<>(allShows, HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.error("Error parsing response from TV Maze API: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing TV shows data.");
        }
    }
}