package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.ErrorObject;
import models.PlayingSong;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
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
        try {
            //getting the recommendation from spotify API by sending GET request
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
            ResponseEntity<String> resEntity = new RestTemplate().exchange("http://api.sr.se/api/v2/playlists/" +
                            "rightnow?channelid="+msg.getChannelID()+"&format=JSON",
                    HttpMethod.GET, reqEntity, String.class);
            System.out.println("request skickades");
            System.out.println(resEntity.getStatusCode());
            //Json parser to parse the API response in JSON
            JsonParser parser = new JsonParser();
            JsonObject playlistObject = parser.parse(resEntity.getBody()).getAsJsonObject().get("playlist").getAsJsonObject();

            String currentSongName = null, currentSongArtist = null;

            //protect from eventual null values if no song is currently plaing on radio channel
            try {
                JsonObject currentSongObject = playlistObject.get("song").getAsJsonObject();
                currentSongName = currentSongObject.get("title").getAsString();
                currentSongArtist = currentSongObject.get("artist").getAsString();
            }catch (NullPointerException e){
                return new Gson().toJson(new ErrorObject(400, "make sure channelID is a valid ID"));
            }

            //If there is no next song catch it and set time of next song to -1
            JsonObject nextSongObject = null;
            String nextSongStartTime;
            PlayingSong song = null;
            boolean nextSongExists = true;
            try{
                nextSongObject = playlistObject.get("nextsong").getAsJsonObject();
            } catch (Exception e){
                nextSongExists = false;
            }
            if(nextSongExists){
                nextSongStartTime = nextSongObject.get("starttimeutc").getAsString();
                song = new PlayingSong(currentSongName, currentSongArtist, nextSongStartTime);
            } else {
                song = new PlayingSong(currentSongName,currentSongArtist,"-1");
            }


            return new Gson().toJson(song);
        }catch (RestClientException e){
            int statusCode = Integer.parseInt(e.getMessage().substring(0, e.getMessage().indexOf(" ")));
            return new Gson().toJson(new ErrorObject(statusCode, e.getMessage()));
        }
    }
}
