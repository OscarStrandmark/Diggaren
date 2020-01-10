package controllers;

import com.google.gson.Gson;
import models.GetPlaylist;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SpotifyGetPlaylistController {
    public String getPlayList(String json) {
        GetPlaylist getPlaylistData = new Gson().fromJson(json, GetPlaylist.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + getPlaylistData.getAuth());
        headers.add("Content-Type","application/json");
        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/users/" + getPlaylistData.getUser_id() + "/playlists", HttpMethod.GET, reqEntity, String.class);
        return resEntity.getBody();
    }
}
