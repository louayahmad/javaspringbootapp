package com.app.reminder.endpoints.episodes;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.app.reminder.endpoints.episodes.helpers.EpisodeDateTimeChecker;
import com.app.reminder.endpoints.episodes.payloads.api.TVMazeAPIEpisodes;
import com.app.reminder.endpoints.episodes.payloads.request.EpisodeRequest;
import com.app.reminder.endpoints.episodes.payloads.response.EpisodeResponse;
import com.app.reminder.endpoints.tvshows.TVShow;
import com.app.reminder.endpoints.tvshows.TVShowRepository;
import com.app.reminder.utils.services.HttpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Fetches upcoming episodes for a TV show by its ID, interacts with the TVMaze
 * API, and saves new episodes that are not already in the database.
 */
@RestController
@RequestMapping
@Tag(name = "Episodes", description = "Get upcoming TV show episodes.")
public class EpisodeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpisodeController.class);

    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private TVShowRepository tvShowRepository;
    @Autowired
    private HttpRequest httpRequest;

    private static final String TVMAZE_API_BASE_URL = "https://api.tvmaze.com";
    private static final String API_KEY = "";

    @PostMapping("/episodes")
    public ResponseEntity<List<EpisodeResponse>> getTVShowEpisodes(@RequestBody EpisodeRequest episodeRequest) {
        LOGGER.info("Fetching all upcoming episodes for the tv show id {}", episodeRequest.getTvShowId());
        LOGGER.info("Received EpisodeRequest object: {}", episodeRequest);

        TVShow tvShow = tvShowRepository.findById(episodeRequest.getTvShowId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "TV Show not found."));

        // Get the episodes for the show using the TV Maze API show id.
        String tvMazeAPIUrlEpisodes = TVMAZE_API_BASE_URL + "/shows/"
                + tvShow.getTVMazeShowId().toString() + "/episodes";
        ResponseEntity<String> episodesResponse = httpRequest.get(tvMazeAPIUrlEpisodes, null, API_KEY, null);

        if (episodesResponse.getStatusCode() != HttpStatus.OK) {
            LOGGER.error("Request to TV Maze API failed with status code: {}", episodesResponse.getStatusCode());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "TV Maze API failed.");
        }

        String tvMazeAPIEpisodes = episodesResponse.getBody();
        if (tvMazeAPIEpisodes == null) {
            LOGGER.error("Received empty response from TV Maze API.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Show not found in TV Maze API.");
        }

        ObjectMapper objectMapperEpisodes = new ObjectMapper();
        List<EpisodeResponse> upcomingEpisodes = new ArrayList<>();

        Set<String> existingUpcomingEpisodes = new HashSet<>();
        List<Episode> newUpcomingEpisodesToSave = new ArrayList<>();

        try {
            List<TVMazeAPIEpisodes> episodesArray = objectMapperEpisodes.readValue(tvMazeAPIEpisodes,
                    new TypeReference<List<TVMazeAPIEpisodes>>() {
                    });

            for (Episode episode : episodeRepository.findAllById(List.of(episodeRequest.getTvShowId()))) {
                existingUpcomingEpisodes.add(episode.getId());
            }

            for (TVMazeAPIEpisodes episodesAPI : episodesArray) {
                Map<Boolean, ZonedDateTime> episodeDateTimeInfo = EpisodeDateTimeChecker
                        .isEpisodeInFuture(episodesAPI.getAirdate(), episodesAPI.getAirtime());

                boolean isFutureEpisode = episodeDateTimeInfo.keySet().iterator().next();
                ZonedDateTime episodeDateTime = episodeDateTimeInfo.values().iterator().next();

                String episodeId = episodesAPI.getName().replace(" ", "_").toLowerCase();

                if (isFutureEpisode) {
                    EpisodeResponse episodeResponse = new EpisodeResponse(episodeId, episodesAPI.getName(),
                            episodeDateTime);

                    upcomingEpisodes.add(episodeResponse);

                    if (!existingUpcomingEpisodes.contains(episodeId)) {
                        Episode newEpisode = new Episode(episodeId, episodesAPI.getName(), episodeDateTime, tvShow);
                        newUpcomingEpisodesToSave.add(newEpisode);
                    }
                }

            }

            episodeRepository.saveAll(newUpcomingEpisodesToSave);

        } catch (JsonProcessingException e) {
            LOGGER.error("Error processing the JSON response from TV Maze API", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error processing TV Maze API response");
        }

        return new ResponseEntity<>(upcomingEpisodes, HttpStatus.OK);
    }
}
