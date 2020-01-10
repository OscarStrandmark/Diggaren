package controllers;

import com.google.gson.*;
import models.Album;
import models.Message;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SpotifyGetAlbum {

    private String albumID = "";

    public SpotifyGetAlbum(){
    }

    public String getAlbum ( Message trackMessage ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+trackMessage.getAuthorization());
        headers.add("Content-Type","application/json");
        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/tracks/"+ trackMessage.getTrackID(),
                HttpMethod.GET, reqEntity, String.class);

        JsonParser parser = new JsonParser();
        JsonObject trackInfo = parser.parse(resEntity.getBody()).getAsJsonObject();
        JsonObject albumInfo = trackInfo.get("album").getAsJsonObject();

        String albumType = albumInfo.get("album_type").getAsString();


        String albumName = albumInfo.get("name").getAsString();
        String albumID = albumInfo.get("id").getAsString();
        JsonArray artistsArray = albumInfo.get("artists").getAsJsonArray(); // Takes the array JsonArray containing the artists.
        String[] artistNames = new String[artistsArray.size()];

        for(int i = 0; i<artistsArray.size(); i++){
            artistNames[i] = artistsArray.get(i).getAsJsonObject().get("name").getAsString();
        }

        Album album = new Album(albumName, artistNames, albumID, albumType);
        Gson gson = new Gson();

        String albumAsJson = gson.toJson(album);
        System.out.println(albumAsJson);

        return albumAsJson;
    }


    public static void main(String[] args) {
        SpotifyGetAlbum a = new SpotifyGetAlbum();
        Message m = new Message();
        a.getAlbum(m);
    }
}
