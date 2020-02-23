package controllers;

import com.google.gson.Gson;
import models.AddToLibrary;

import models.ErrorObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;

/**
 * This class is responsible for adding a song to a users library.
 *
 * @Author Oscar Strandmark
 */
public class AddToLibraryController {
    public String addToLibrary(String json){
        //Get input as object
        AddToLibrary libraryData = new Gson().fromJson(json,AddToLibrary.class);
        //Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + libraryData.getAuth());
        //Do the api call to spotify and return the response.
        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/me/tracks?ids=" + libraryData.getTrack_id(), HttpMethod.PUT ,reqEntity ,String.class);

        if(resEntity.getStatusCode() != HttpStatus.valueOf(200)){
            Gson gson = new Gson();
            return gson.toJson( new ErrorObject(resEntity.getStatusCodeValue()));
        }
        return resEntity.getBody();
    }
}
