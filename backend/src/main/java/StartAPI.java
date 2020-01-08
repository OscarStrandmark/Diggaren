import controllers.SpotifyGetPlaylist;

import static spark.Spark.*;

public class StartAPI {
    public static void main(String[] args) {
        port(5050);

        SpotifyGetPlaylist spotifyGetPlaylist = new SpotifyGetPlaylist();


        path("/spotify", () -> {
            get("getPlaylist",((request, response) -> spotifyGetPlaylist.getPlayList(request.attribute("auth"))));

        });
    }
}
