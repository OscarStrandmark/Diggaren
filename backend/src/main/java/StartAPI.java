import static spark.Spark.*;

public class StartAPI {
    public static void main(String[] args) {
        port(5050);

        Spotify spotify = new Spotify();
        SR sr = new SR();

        get("/",(request,response) -> {
            return sr.getCurrentlyPlaying(request.attribute("channelID"));
        });


        path("/spotify", () -> {
            //Auth
            get("/auth/getAuthCode",   (req, res) ->   spotify.requestAuthCode(req.params("clientId")));
            get("/auth/getToken/:code/:cId/:cS", (req, res) -> spotify.requestAccessToken(req.params(":code"), req.params(":clientId"), req.params(":clientSecret")));

            //Search endpoints
            path("/search", () -> {
                get("/song", (req,res) -> spotify.searchSong(req.params("auth"),req.params("type"),req.params("query")));
            });
        });
    }
}
