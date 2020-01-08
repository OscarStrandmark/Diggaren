package controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SpotifySearch {
    /**
     *
     * @param auth auth code
     * @param type What type of media you search for. Valid types are: "album", "artist" & "track"
     * @param query The search query
     * @return
     */
    public String search(String auth,String type,String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", auth);

        String searchQuery = "";

        for (int i = 0; i < query.length(); i++) {
            query.replaceAll(" ","+");
        }

        HttpEntity<String> reqEntity = new HttpEntity<String>("",headers);
        ResponseEntity<String> resEntity = new RestTemplate().exchange("https://api.spotify.com/v1/search", HttpMethod.GET, reqEntity, String.class);

        return resEntity.getBody();
    }
}
