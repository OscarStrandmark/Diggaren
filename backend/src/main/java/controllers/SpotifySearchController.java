package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import models.Search;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SpotifySearchController {

    public String search(String json){
        Search searchData = new Gson().fromJson(json, Search.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + searchData.getAuth());
        headers.add("Content-Type","application/json");

        String searchQuery = "";

        for (int i = 0; i < searchData.getQuery().length(); i++) {
            searchData.getQuery().replaceAll(" ","+");
        }

        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/search?" + "q=" + searchData.getQuery() + "&" + "type=" + searchData.getType(),HttpMethod.GET ,reqEntity ,String.class);

        return resEntity.getBody();
    }
}
