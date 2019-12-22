package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SpotifyGetAlbum {

    public JsonObject getAlbum(String albumId ) throws IOException {
        URL url = new URL("https://api.spotify.com/v1/albums/"+ albumId );
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization" , "b93a3bb4485241cd99858866d868516e");
        con.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer(); // i content får jag mitt svar.
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        //Gson file = new Gson(content,);  // gå in i album klassen och modifiera Jsonobjectet


        return null; // returnera det nya Json objektet här.
    }

}
