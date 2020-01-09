package controllers;

import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import models.AddToPlaylist;

public class SpotifyAddToPlaylistController {
    public String addToPlayList(String json){
        AddToPlaylist addToPlaylist = new Gson().fromJson(json,AddToPlaylist.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", addToPlaylist.getAuth());
        headers.add("Content-Type","application/json");

        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/playlists/" + addToPlaylist.getPlaylist_id() + "{playlist_id}/tracks?uris=" + addToPlaylist.getTrack_id()
                                                                            , HttpMethod.POST ,reqEntity ,String.class);

        return resEntity.getBody();
    }
}
