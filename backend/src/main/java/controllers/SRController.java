package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.ErrorObject;
import models.PlayingSong;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import models.SRMessage;

/**
 * Controller class for "currentlyPlaying" endpoint. Sends API Request
 * to Sveriges Radio API and fetches and process the received data.
 */
public class SRController {

    /**
     * Get currently playing song from Sveriges Radio API by
     * specified radio channel, returns an PlayingSong object.
     * If no current song is played, artist and song parameters of PlayingSong
     * will be null and only nextSongStartTime will have an value.
     * @param msg - SRMessage containing requested radio channel
     * @return - PlayingSong containing information about current playing song and
     * time of next song starting.
     */
    public String getSongPlaying(SRMessage msg){
        //getting the recommendation from spotify API by sending GET request
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("http://api.sr.se/api/v2/playlists/" +
                        "rightnow?channelid="+msg.getChannelID()+"&format=JSON",
                HttpMethod.GET, reqEntity, String.class);

        if(resEntity.getStatusCode() != HttpStatus.OK){
            return new Gson().toJson(new ErrorObject(resEntity.getStatusCodeValue()));
        }

        //Json parser to parse the API response in JSON
        JsonParser parser = new JsonParser();
        JsonObject playlistObject = parser.parse(resEntity.getBody()).getAsJsonObject().get("playlist").getAsJsonObject();

        String currentSongName = null, currentSongArtist = null;

        //protect from eventual null values if no song is currently plaing on radio channel
        try {
            JsonObject currentSongObject = playlistObject.get("song").getAsJsonObject();
            currentSongName = currentSongObject.get("title").getAsString();
            currentSongArtist = currentSongObject.get("artist").getAsString();
        }catch (NullPointerException e){}

        JsonObject nextSongObject = playlistObject.get("nextsong").getAsJsonObject();
        String nextSongStartTime = nextSongObject.get("starttimeutc").getAsString();
        PlayingSong song = new PlayingSong(currentSongName, currentSongArtist, nextSongStartTime);

        return new Gson().toJson(song);
    }
}
