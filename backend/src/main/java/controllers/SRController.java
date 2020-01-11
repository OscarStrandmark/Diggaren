package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.PlayingSong;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import models.SRMessage;

public class SRController {

    public PlayingSong getSongPlaying(SRMessage msg){
        //getting the recommendation from spotify API by sending GET req
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("http://api.sr.se/api/v2/playlists/" +
                        "rightnow?channelid="+msg.getChannelID()+"&format=JSON",
                HttpMethod.GET, reqEntity, String.class);

        JsonParser parser = new JsonParser();
        JsonObject playlistObject = parser.parse(resEntity.getBody()).getAsJsonObject().get("playlist").getAsJsonObject();

        String currentSongName = null, currentSongArtist = null;

        try {
            JsonObject currentSongObject = playlistObject.get("song").getAsJsonObject();
            currentSongName = currentSongObject.get("title").getAsString();
            currentSongArtist = currentSongObject.get("artist").getAsString();
        }catch (NullPointerException e){}

        JsonObject nextSongObject = playlistObject.get("nextsong").getAsJsonObject();
        String nextSongStartTime = nextSongObject.get("starttimeutc").getAsString();
        PlayingSong song = new PlayingSong(currentSongName, currentSongArtist, nextSongStartTime);

        return song;
    }
}
