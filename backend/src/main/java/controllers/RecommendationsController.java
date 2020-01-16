package controllers;

import com.google.gson.*;
import models.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import models.TrackMessage;

/**
 * Controller clas for "Spotify/recommendations" endpoint. Working towards
 * Spotify API and process the response and package it into an Recommendation
 * object for the requesting client.
 */
public class RecommendationsController {

    public RecommendationsController() {
    }

    /**
     * Request an recommended song from Spotify API based on queried song
     * packages into Recommendation object
     * @param msg - TrackMessage containing an song ID (from Spotify) and
     * Authorization token (from Spotify).
     * @return - Recommendation object containing processed and packaged data
     * from Spotify API
     */
    public Recommendation getRecommendation(TrackMessage msg) {
        //getting the recommendation from spotify API by sending GET req
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+msg.getAuth());
        headers.add("Content-Type","application/json");
        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/recommendations?" +
                        "limit=1&market=SE&seed_tracks="+ msg.getTrackID() +
                        "&min_energy=0.4&min_popularity=50",
                HttpMethod.GET, reqEntity, String.class);


        //since the response is a nestled json object and we need to convert it to an map and fetch the wanted data
        JsonParser parser = new JsonParser();
        JsonObject trackInfo = parser.parse(resEntity.getBody()).getAsJsonObject().get("tracks").getAsJsonArray().get(0).getAsJsonObject(); //gets the one and only track from the response as an json object
        String trackID = trackInfo.get("id").getAsString();
        String trackName = trackInfo.get("name").getAsString();
        JsonObject artistInfo = trackInfo.getAsJsonArray("artists").get(0).getAsJsonObject();
        String artistName = artistInfo.get("name").getAsString();
        String artistID = artistInfo.get("id").getAsString();

        System.out.println(artistName + " - " + trackName);

        return new Recommendation(trackName, trackID, artistName, artistID);
    }


}
