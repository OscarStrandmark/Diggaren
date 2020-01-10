package controllers;

import com.google.gson.Gson;
import models.AddToLibrary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class AddToLibraryController {
    /*

    Får rätt statuskod på rad 23, men den visas fel i postman ?

     */
    public String addToLibrary(String json){
        AddToLibrary libraryData = new Gson().fromJson(json,AddToLibrary.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + libraryData.getAuth());
        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/me/tracks?ids=" + libraryData.getTrack_id(), HttpMethod.PUT ,reqEntity ,String.class);
        return resEntity.getBody();
    }
}
