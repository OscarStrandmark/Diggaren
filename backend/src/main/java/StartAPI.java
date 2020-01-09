import controllers.SpotifySearch;
import controllers.SpotifySearchController;

import static spark.Spark.*;

public class StartAPI {
    public static void main(String[] args) {
        port(5050);

        SpotifySearchController spotifySearch = new SpotifySearchController();

        path("/spotify", () -> {

            //Search endpoints
            path("/search", () -> {
                get("/", (req,res) -> spotifySearch.search(req.params("auth"),req.params("type"),req.params("query")));
            });
        });
    }
}
