package com.app.reminder.endpoints.episodes;

import java.time.ZonedDateTime;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.app.reminder.endpoints.episodes.helpers.EpisodeDateTimeChecker;
import com.app.reminder.endpoints.episodes.payloads.api.TVMazeAPIEpisodes;
import com.app.reminder.endpoints.episodes.payloads.api.TVMazeAPITVShowSearch;
import com.app.reminder.endpoints.episodes.payloads.request.EpisodeRequest;
import com.app.reminder.endpoints.episodes.payloads.response.EpisodeResponse;
import com.app.reminder.endpoints.tvshows.TVShow;
import com.app.reminder.endpoints.tvshows.TVShowRepository;
import com.app.reminder.utils.services.HttpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping
@Tag(name = "Episodes", description = "Get upcoming TV show episodes.")
public class EpisodeController {

    private static final Logger logger = LoggerFactory.getLogger(EpisodeController.class);

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
        logger.info("Fetching all upcoming episodes for the tv show id {}", episodeRequest.getTvShowId());

        TVShow tvShow = tvShowRepository.findById(episodeRequest.getTvShowId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "TV Show not found."));

        String tvMazeAPIUrlShows = TVMAZE_API_BASE_URL + "/search/shows";
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("q", episodeRequest.getTvShowName());
        
        logger.info("Sending request to TV Maze API with parameters: {}", queryParams);
        ResponseEntity<String> showsResponse = httpRequest.get(tvMazeAPIUrlShows, null, API_KEY, queryParams);
    
        if (showsResponse.getStatusCode() != HttpStatus.OK) {
            logger.error("Request to TV Maze API failed with status code: {}", showsResponse.getStatusCode());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "TV Maze API failed.");
        }

        String tvMazeAPIShows = showsResponse.getBody();
        if (tvMazeAPIShows == null) {
            logger.error("Received empty response from TV Maze API.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Show not found in TV Maze API.");
        }

        ObjectMapper objectMapperShows = new ObjectMapper();
        Map<String, Integer> idToName = new HashMap<>();
        try {
            List<TVMazeAPITVShowSearch> showsArray = objectMapperShows.readValue(tvMazeAPIShows, new TypeReference<List<TVMazeAPITVShowSearch>>() {});

            for (TVMazeAPITVShowSearch showAPI : showsArray) {
                idToName.put(showAPI.getShow().getName(), showAPI.getShow().getId());
            }

            if (!idToName.containsKey(episodeRequest.getTvShowName())){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Show not found in TV Maze API.");
            }
    
        } catch (JsonProcessingException e) {
            logger.error("Error processing the JSON response from TV Maze API", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing TV Maze API response");
        }
        
        // Get the episodes for the show using the TV Maze API show id.

        String tvMazeAPIUrlEpisodes = TVMAZE_API_BASE_URL + "/shows/" + idToName.get(episodeRequest.getTvShowName()).toString() + "/episodes";
        ResponseEntity<String> episodesResponse = httpRequest.get(tvMazeAPIUrlEpisodes, null, API_KEY, null);

        if (episodesResponse.getStatusCode() != HttpStatus.OK) {
            logger.error("Request to TV Maze API failed with status code: {}", episodesResponse.getStatusCode());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "TV Maze API failed.");
        }

        String tvMazeAPIEpisodes = episodesResponse.getBody();
        if (tvMazeAPIEpisodes == null) {
            logger.error("Received empty response from TV Maze API.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Show not found in TV Maze API.");
        }

        ObjectMapper objectMapperEpisodes = new ObjectMapper();
        List<EpisodeResponse> upcomingEpisodes = new ArrayList<>();
        
        Set<String> existingUpcomingEpisodes = new HashSet<>();
        List<Episode> newUpcomingEpisodesToSave = new ArrayList<>();

        try {
            List<TVMazeAPIEpisodes> episodesArray = objectMapperEpisodes.readValue(tvMazeAPIEpisodes, new TypeReference<List<TVMazeAPIEpisodes>>() {});
            
            for (Episode episode: episodeRepository.findAllById(List.of(episodeRequest.getTvShowId()))){
                existingUpcomingEpisodes.add(episode.getId());
            }

            for (TVMazeAPIEpisodes episodesAPI : episodesArray) {
                Map<Boolean, ZonedDateTime> episodeDateTimeInfo = EpisodeDateTimeChecker.isEpisodeInFuture(episodesAPI.getAirdate(), episodesAPI.getAirtime());
                
                Boolean isFutureEpisode = null;
                ZonedDateTime episodeDateTime = null;
                for (Map.Entry<Boolean, ZonedDateTime> entry : episodeDateTimeInfo.entrySet()) {
                    isFutureEpisode = entry.getKey();
                    episodeDateTime = entry.getValue();
                }

                String episodeId = episodesAPI.getName().replace(" ", "_").toLowerCase();

                if (isFutureEpisode == true){
                    EpisodeResponse episodeResponse = new EpisodeResponse();
                    episodeResponse.setEpisodeId(episodeId);
                    episodeResponse.setEpisodeName(episodesAPI.getName());
                    episodeResponse.setEpisodeAirDateTime(episodeDateTime);
                    
                    upcomingEpisodes.add(episodeResponse);

                    if (!existingUpcomingEpisodes.contains(episodeId)){
                        Episode newEpisode = new Episode();
                        newEpisode.setId(episodeId);
                        newEpisode.setName(episodesAPI.getName());
                        newEpisode.setDateTime(episodeDateTime);
                        newEpisode.setTvShow(tvShow);

                        newUpcomingEpisodesToSave.add(newEpisode);
                    }
                }

            }
            
            episodeRepository.saveAll(newUpcomingEpisodesToSave);

        } catch (JsonProcessingException e) {
            logger.error("Error processing the JSON response from TV Maze API", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing TV Maze API response");
        }
        
        return new ResponseEntity<>(upcomingEpisodes, HttpStatus.OK);
    }
}
