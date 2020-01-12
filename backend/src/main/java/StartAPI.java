import com.google.gson.Gson;
import controllers.*;
import models.SRMessage;
import models.TrackMessage;
import util.CorsFilter;

import static spark.Spark.*;

public class StartAPI {

    public static void main(String[] args) {
        port(5050);

        CorsFilter.apply();

        Gson gson = new Gson();

        //Controllers
        SpotifyAddToPlaylistController spotifyAddToPlaylistController = new SpotifyAddToPlaylistController();
        RecommendationsController recommendationsController = new RecommendationsController();
        SpotifyGetPlaylistController spotifyGetPlaylist = new SpotifyGetPlaylistController();
        SpotifySearchController spotifySearchController = new SpotifySearchController();
        AudioFeaturesController audioFeaturesController = new AudioFeaturesController();
        PseudoChannelController psuedoChannelController = new PseudoChannelController();
        SpotifyGetAlbumController getAlbumController = new SpotifyGetAlbumController();
        AddToLibraryController addToLibraryController = new AddToLibraryController();
        SRController srController = new SRController();

        //Endpoints relating to spotify
        path("/spotify", () -> {
            //Search through the spotify-API
            post("/search", (request,response) -> {
                response.type("application/json"); //definiera svar som json
                return spotifySearchController.search(request.body());
            });
            //Add a track to your spotify library
            post("/AddToLibrary", (request,response) -> addToLibraryController.addToLibrary(request.body()));
            //Endpoints concerning playlists
            path("/playlist", () -> {
                //Get a list of the users spotify playlists.
                post("/fetch", (request,response) -> {
                    response.type("application/json"); //definiera svar som json
                    return spotifyGetPlaylist.getPlayList(request.body());
                });
                //Add a song to a playlist
                post("/add", (request,response) -> spotifyAddToPlaylistController.addToPlayList(request.body()));
            });
            //Get album
            post("/album", (request, response) -> {
                return getAlbumController.getAlbum(request.body());
            });
          
            //MARK: Recommendations
            post("/recommendation", (request, response) -> {
                response.type("application/json"); //definiera svar som json
                TrackMessage msg = gson.fromJson(request.body(), TrackMessage.class); //hämta json object från body som ett definierat objekt
                return recommendationsController.getRecommendation(msg);
            }, gson :: toJson);

            //MARK: AudioFeatures
            post("/audioFeatures", (request, response) -> {
                response.type("application/json"); //definiera svar som json
                TrackMessage msg = gson.fromJson(request.body(), TrackMessage.class); //hämta json object från body som ett definierat objekt
                return audioFeaturesController.getAudioFeatures(msg);
            }, gson :: toJson);
        });

        path("/SR", () -> {
            //MARK: SR spelas nu
            post("/currentlyPlaying", (request, response) -> {
                response.type("application/json"); //definiera svar som json
                SRMessage msg = gson.fromJson(request.body(), SRMessage.class); //hämta json object från body som ett definierat objekt
                return srController.getSongPlaying(msg);
            }, gson :: toJson);
        });

        post("psuedoChannel",((request, response) -> {
            response.type("application/json"); //definiera svar som json
            return psuedoChannelController.getChannel(request.body());
        }), gson :: toJson);


        options("/*", (request, response) -> {


            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            String accessControlRequestOrigin = request.headers("Access-Control-Request-Origin");
            if (accessControlRequestOrigin != null){
                response.header("Access-Control-Allow-Origin", "*");
            }

            return "OK";
        });
    }
}
