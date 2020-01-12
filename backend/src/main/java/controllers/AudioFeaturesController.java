package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import models.TrackMessage;

/**
 * Controller class for handling "Spotify/audioFeatures" endpoint,
 * sends request to Spotify API and process the received data and
 * package it to an AudioFeatures object.
 */
public class AudioFeaturesController {

    public AudioFeaturesController() {
    }

    /**
     * Sending request for audioFeatures of seeded song to Spotify API
     * @param msg - TrackMessage containgin Autorization token and songID
     * (from Spotify)
     * @return - AudioFeatures object containing values of different audio
     * meassures.
     */
    public AudioFeatures getAudioFeatures(TrackMessage msg) {
        //getting the recommendation from spotify API by sending GET req
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+msg.getAuthorization());
        headers.add("Content-Type","application/json");
        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange(
                "https://api.spotify.com/v1/audio-features/"+msg.getTrackID(),
                HttpMethod.GET, reqEntity, String.class);

        //Parse the answer in JSON
        JsonParser parser = new JsonParser();

        JsonObject featuresObject = parser.parse(resEntity.getBody()).getAsJsonObject();
        AudioFeatures audioFeatures = new AudioFeatures(
            featuresObject.get("danceability").getAsString(),
                featuresObject.get("energy").getAsString(),
                featuresObject.get("loudness").getAsString(),
                featuresObject.get("speechiness").getAsString(),
                featuresObject.get("acousticness").getAsString(),
                featuresObject.get("instrumentalness").getAsString(),
                featuresObject.get("liveness").getAsString(),
                featuresObject.get("valence").getAsString(),
                featuresObject.get("tempo").getAsString()
        );
        return audioFeatures;
    }
}
