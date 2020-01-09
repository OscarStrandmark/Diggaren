package controllers;

import com.google.gson.*;
import models.Album;
import models.Message;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;


public class SpotifyGetAlbum {

    public SpotifyGetAlbum(){

    }

    public String getAlbum(Message message ){
        //getting the recommendation from spotify API by sending GET req
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+message.getAuthorization());
        headers.add("Content-Type","application/json");
        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/albums/"+ message.getTrackID(),
                HttpMethod.GET, reqEntity, String.class);


        JsonParser parser = new JsonParser();
        JsonObject albumInfo = parser.parse(resEntity.getBody()).getAsJsonObject();

        String albumType = albumInfo.get("album_type").getAsString();
        String albumName = albumInfo.get("name").getAsString();
        String label = albumInfo.get("label").getAsString();
        String albumID = albumInfo.get("id").getAsString();

        JsonArray artistsArray = albumInfo.get("artists").getAsJsonArray();
        String artistName = artistsArray.get(0).getAsJsonObject().get("name").getAsString();

//        System.out.println(albumInfo);
//        System.out.println(albumType + ", " + albumName + ", " + label + ", " + albumID + ", " + artistName);

        Album album = new Album(albumName, artistName, label, albumID, albumType);
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
