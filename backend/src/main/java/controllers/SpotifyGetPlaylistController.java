package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import models.GetPlaylist;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * This class is responsible for getting the playlists a given user has on their account
 *
 * @Author Oscar Strandmark
 *
 */
public class SpotifyGetPlaylistController {
    public String getPlayList(String json) {
        //Get inputdata
        GetPlaylist getPlaylistData = new Gson().fromJson(json, GetPlaylist.class);
        HttpHeaders headers = new HttpHeaders();
        //set headers
        headers.add("Authorization","Bearer " + getPlaylistData.getAuth());
        headers.add("Content-Type","application/json");

        //Get userId
        HttpEntity<String> reqEntityGetUserId = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntityGetUserId = new RestTemplate().exchange("https://api.spotify.com/v1/me", HttpMethod.GET, reqEntityGetUserId, String.class);

        //Parse userID
        JsonParser parser = new JsonParser();
        String response = resEntityGetUserId.getBody();
        String userID = parser.parse(response).getAsJsonObject().get("id").getAsString();

        //Get list of playlists
        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/users/" + userID + "/playlists", HttpMethod.GET, reqEntity, String.class);
        return resEntity.getBody();
    }
}
