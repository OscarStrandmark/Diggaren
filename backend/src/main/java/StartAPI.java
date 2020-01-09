import controllers.SpotifyGetAlbum;
import models.Message;

import static spark.Spark.port;
import static spark.Spark.get;

public class StartAPI {
    public static void main(String[] args) {
        port(5050);

        Message msg = new Message();
        SpotifyGetAlbum get = new SpotifyGetAlbum();
        port(5050);

        get("/album", (request, response) -> {
            return get.getAlbum(msg);
                });


//        Spotify spotify = new Spotify();
//        SR sr = new SR();
//
//        get("/",(request,response) -> {
//            return sr.getCurrentlyPlaying(request.attribute("channelID"));
//        });

    }
}
