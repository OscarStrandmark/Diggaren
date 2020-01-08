package controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class SpotifyAuth {
    private String redirectURL = ""; //TODO:ADD URL

    /**
     * Request for auth code & scope confirmation in client
     *
     * @param clientId
     * @return url for scope confirmation
     */
    public String requestAuthCode(String clientId) {
        String accessTokenUrl = "https://accounts.spotify.com/authorize";
        String response = String.format("%s?client_id=%s&response_type=code&redirect_uri=%s",accessTokenUrl, clientId,redirectURL);
        return response;
    }

    /**
     * Requests and returns access- and refresh-tokens
     * @param code
     * @param clientId
     * @param clientSecret
     * @return
     */
    public String requestAccessToken(String code, String clientId, String clientSecret) {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String accessToken = null;
        MultiValueMap<String,String> map = new LinkedMultiValueMap<String,String>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", redirectURL);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String,String>> reqEntity = new HttpEntity<MultiValueMap<String, String>>(map,headers);

        try {
            accessToken = template.postForObject("https://accounts.spotify.com/api/token",reqEntity,String.class);
        }catch (RestClientResponseException e){
            e.printStackTrace();
        }

        return accessToken;
    }
}
