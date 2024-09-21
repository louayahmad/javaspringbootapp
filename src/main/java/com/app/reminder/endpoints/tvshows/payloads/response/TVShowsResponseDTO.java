package com.app.reminder.endpoints.tvshows.payloads.response;

import java.util.List;

import com.app.reminder.endpoints.tvshows.payloads.response.helpers.EpisodesDTO;
import com.app.reminder.endpoints.tvshows.payloads.response.helpers.GenresDTO;

import lombok.Data;

@Data                        
public class TVShowsResponseDTO {
    private String id; 
    private String showName;
    private String language;
    private String premiered;
    private String ended;
    private String network;
    private List<GenresDTO> genres;
    private List<EpisodesDTO> episodes;
}

