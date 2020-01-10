import com.google.gson.Gson;
import util.SRMessage;
import util.TrackMessage;
import controllers.*;
import static spark.Spark.*;

public class StartAPI {

    public static void main(String[] args) {
        port(5050);
        Gson gson = new Gson();

        //Controllers
        RecommendationsController recommendationsController = new RecommendationsController();
        AudioFeaturesController audioFeaturesController = new AudioFeaturesController();
        SRController srController = new SRController();

        

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

        //MARK: Recommendations
        post("/recommendations", (request, response) -> {
            response.type("application/json"); //definiera svar som json
            TrackMessage msg = gson.fromJson(request.body(), TrackMessage.class); //hämta json object från body som ett definierat objekt
            return recommendationsController.getRecommendation(msg);
        }, gson :: toJson);

        //MARK: AudioFeatures
        post("/audio_features", (request, response) -> {
            response.type("application/json"); //definiera svar som json
            TrackMessage msg = gson.fromJson(request.body(), TrackMessage.class); //hämta json object från body som ett definierat objekt
            return audioFeaturesController.getAudioFeatures(msg);
        }, gson :: toJson);

        //MARK: SR spelas nu
        post("/playing_song", (request, response) -> {
            response.type("application/json"); //definiera svar som json
            SRMessage msg = gson.fromJson(request.body(), SRMessage.class); //hämta json object från body som ett definierat objekt
            return srController.getSongPlaying(msg);
        }, gson :: toJson);
    }
}
