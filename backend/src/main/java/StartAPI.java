import controllers.*;

import static spark.Spark.*;

public class StartAPI {
    public static void main(String[] args) {
        port(5050);

        SpotifyAddToPlaylistController spotifyAddToPlaylistController = new SpotifyAddToPlaylistController();

        path("/spotify", () -> {

            //Playlist-endpoints
            path("/playlist", () -> {
                post("/addToPlaylist", (req,res) -> {
                    return spotifyAddToPlaylistController.addToPlayList(req.body());
                });
            });
        });


    }
}
