package com.app.reminder.endpoints.tvshows.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                    
@AllArgsConstructor    
@NoArgsConstructor
public class TVShowsResponse {
    private String id; 
    private String showName;
    private String language;
    private String premiered;
    private String status;
    private String network;
}

