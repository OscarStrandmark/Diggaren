package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import models.ErrorObject;
import models.GetPlaylist;
import org.springframework.http.*;
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
        HttpEntity<String> requestEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> responseEntity = new RestTemplate().exchange("https://api.spotify.com/v1/me/playlists", HttpMethod.GET, requestEntity, String.class);

        if(responseEntity.getStatusCode() != HttpStatus.valueOf(200)){
            Gson gson = new Gson();
            return gson.toJson( new ErrorObject(responseEntity.getStatusCodeValue()));
        }
        return responseEntity.getBody();
    }
}
