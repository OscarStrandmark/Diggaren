package controllers;

import com.google.gson.*;
import models.Album;
import models.ErrorObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import models.TrackMessage;

/**
 * This class is responsible for fetching a JSON file representing an album from Spotify.
 * The class also modifies the data received, and returns an {@link Album} as a JSON String to the responsible endpoint.
 *
 * @author Said Mohammed
 *
 */

public class SpotifyGetAlbumController {

    private String albumID = "";

    public SpotifyGetAlbumController(){
    }

    /**
     * @param inputJson the parameters that we get from {@link TrackMessage} containing the necessary input parameters
     *                  to make a call to Spotify's API.
     *
     * @return a {@link Album} object converted to JSON.
     *
     */
    public String getAlbum (String inputJson){
        Gson gson = new Gson();
        TrackMessage trackMessage = gson.fromJson(inputJson, TrackMessage.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + trackMessage.getAuth());
        headers.add("Content-Type","application/json");
        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/tracks/"+ trackMessage.getTrackID(),
                HttpMethod.GET, reqEntity, String.class);

        JsonParser parser = new JsonParser();
        JsonObject trackInfo = parser.parse(resEntity.getBody()).getAsJsonObject();
        JsonObject albumInfo = trackInfo.get("album").getAsJsonObject(); // We only use the information stored in the album variable

        // store the data in to our album variables.
        String albumType = albumInfo.get("album_type").getAsString();
        String albumName = albumInfo.get("name").getAsString();
        String albumID = albumInfo.get("id").getAsString();
        JsonArray artistsArray = albumInfo.get("artists").getAsJsonArray(); // Takes the array JsonArray containing the artists.
        String[] artistNames = new String[artistsArray.size()];

        for(int i = 0; i<artistsArray.size(); i++){
            artistNames[i] = artistsArray.get(i).getAsJsonObject().get("name").getAsString();
        }
        Album album = new Album(albumName, artistNames, albumID, albumType);
        String albumAsJson = gson.toJson(album);

        if(resEntity.getStatusCode() != HttpStatus.OK){
            return gson.toJson( new ErrorObject(resEntity.getStatusCodeValue()));
        }

        return albumAsJson;
    }
}
