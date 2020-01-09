import controllers.*;
import static spark.Spark.*;

public class StartAPI {
    public static void main(String[] args) {
        port(5050);

        SpotifyAddToPlaylistController spotifyAddToPlaylistController = new SpotifyAddToPlaylistController();
        SpotifyGetPlaylistController spotifyGetPlaylist = new SpotifyGetPlaylistController();
        SpotifySearchController spotifySearchController = new SpotifySearchController();
        AddToLibraryController addToLibraryController = new AddToLibraryController();

        //Endpoints relating to spotify
        path("/spotify", () -> {
            //Search through the spotify-API
            get("/search", (req,res) -> spotifySearchController.search(req.body()));
            //Add a track to your spotify library
            put("/AddToLibrary", (req,res) -> addToLibraryController.addToLibrary(req.body()));
            //Endpoints concerning playlists
            path("/playlist", () -> {
                //Get a list of the users spotify playlists.
                get("/fetch", (req,res) -> { return spotifyGetPlaylist.getPlayList(req.body()); });
                //Add a song to a playlist
                post("/add", (req,res) -> { return spotifyAddToPlaylistController.addToPlayList(req.body()); });
            });
        });
    }
}
