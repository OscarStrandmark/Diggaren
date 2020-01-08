import controllers.SpotifyAuth;
import controllers.SpotifySearch;

import static spark.Spark.*;

public class StartAPI {
    public static void main(String[] args) {
        port(5050);

        SpotifyAuth spotifyAuth = new SpotifyAuth();
        SpotifySearch spotifySearch = new SpotifySearch();

        path("/spotify", () -> {
            //Auth
            get("/auth/getAuthCode",   (req, res) ->   spotifyAuth.requestAuthCode(req.params("clientId")));
            get("/auth/getToken/:code/:cId/:cS", (req, res) -> spotifyAuth.requestAccessToken(req.params(":code"), req.params(":clientId"), req.params(":clientSecret")));

            //Search endpoints
            path("/search", () -> {
                get("/", (req,res) -> spotifySearch.search(req.params("auth"),req.params("type"),req.params("query")));
            });
        });
    }
}
