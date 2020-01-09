import controllers.*;
import static spark.Spark.*;

public class StartAPI {
    public static void main(String[] args) {
        port(5050);

        SpotifyGetPlaylistController spotifyGetPlaylist = new SpotifyGetPlaylistController();
        SpotifyAddToPlaylistController spotifyAddToPlaylistController = new SpotifyAddToPlaylistController();
        AddToLibraryController addToLibraryController = new AddToLibraryController();
      
        path("/spotify", () -> {
          get("getPlaylist",((request, response) -> spotifyGetPlaylist.getPlayList(request.attribute("auth"))));
        //Search endpoints
            path("/search", () -> {
                get("/", (req,res) -> spotifySearch.search(req.params("auth"),req.params("type"),req.params("query")));
            });
            path("/library", () -> {
                put("/addto", (req,res) -> addToLibraryController.addToLibrary(req.body()));
            });
            path("/playlist", () -> {
                post("/addToPlaylist", (req,res) -> {
                    return spotifyAddToPlaylistController.addToPlayList(req.body());
                });
            });
    }
}
