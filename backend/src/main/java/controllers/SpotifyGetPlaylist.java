package controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SpotifyGetPlaylist {
    public String getPlayList(String auth) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",auth);

        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/search", HttpMethod.GET, reqEntity, String.class);

        return resEntity.getBody();
    }
}
