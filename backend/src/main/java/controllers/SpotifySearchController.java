package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import models.ErrorObject;
import models.Search;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * This class handles searching the spotift catalog.
 *
 * @Authors Oscar Strandmark
 */
public class SpotifySearchController {

    public String search(String json){
        //Convert json to java-object
        Search searchData = new Gson().fromJson(json, Search.class);
        //Add headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + searchData.getAuth());
        headers.add("Content-Type","application/json");

        //Replace whitespace with '+' to follow the rules for spotify's API
        for (int i = 0; i < searchData.getQuery().length(); i++) {
            searchData.getQuery().replaceAll(" ","+");
        }

        //Do call to spotify API
        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/search?" + "q=" + searchData.getQuery() + "&" + "type=" + searchData.getType(),HttpMethod.GET ,reqEntity ,String.class);

        if(resEntity.getStatusCode() != HttpStatus.OK){
            return new Gson().toJson(new ErrorObject(resEntity.getStatusCodeValue()));
        }

        return resEntity.getBody();
    }
}
