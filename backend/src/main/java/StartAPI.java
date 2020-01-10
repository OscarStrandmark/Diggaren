import controllers.SpotifyGetAlbum;
import models.Message;

import static spark.Spark.port;
import static spark.Spark.get;

public class StartAPI {
    public static void main(String[] args) {


        Message msg = new Message();
        SpotifyGetAlbum get = new SpotifyGetAlbum();
        port(5050);

        get("/album", (request, response) -> {

            return get.getAlbum(msg);
                });

    }
}
