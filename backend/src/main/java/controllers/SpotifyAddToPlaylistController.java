package controllers;

import com.google.gson.Gson;
import models.ErrorObject;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import models.AddToPlaylist;

/**
 * Class responsible for adding a song to a playlist on spotfiy.
 *
 * @Author Oscar Strandmark
 */
public class SpotifyAddToPlaylistController {
    public String addToPlayList(String json){
        try {
            //Convert json to object
            AddToPlaylist addToPlaylist = new Gson().fromJson(json, AddToPlaylist.class);
            //Add headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + addToPlaylist.getAuth());
            headers.add("Content-Type", "application/json");
            //Do call to spotify api
            HttpEntity<String> reqEntity = new HttpEntity<String>("", headers);
            ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/playlists/" + addToPlaylist.getPlaylist_id() + "/tracks?uris=spotify:track:" + addToPlaylist.getTrack_id(), HttpMethod.POST, reqEntity, String.class);
            return resEntity.getBody();
        }catch (RestClientException e){
            int statusCode = Integer.parseInt(e.getMessage().substring(0, e.getMessage().indexOf(" ")));
            return new Gson().toJson(new ErrorObject(statusCode, e.getMessage()));
        }
    }
}
