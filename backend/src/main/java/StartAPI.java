import controllers.SpotifyGetPlaylistController;

import static spark.Spark.*;

public class StartAPI {
    public static void main(String[] args) {
        port(5050);

        SpotifyGetPlaylistController spotifyGetPlaylist = new SpotifyGetPlaylistController();


        path("/spotify", () -> {
            get("getPlaylist",((request, response) -> spotifyGetPlaylist.getPlayList(request.attribute("auth"))));

        });
    }
}
