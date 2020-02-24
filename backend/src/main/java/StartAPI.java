import com.google.gson.Gson;
import controllers.*;
import models.SRMessage;
import models.TrackMessage;
import netscape.javascript.JSObject;
import org.codehaus.jackson.map.util.JSONPObject;
import util.CorsFilter;

import static spark.Spark.*;

public class StartAPI {

    public static void main(String[] args) {
        //Run API on port given
        port(5050);

        //Filter class found on github, used to give our responses the headers they needed to fix CORS-errors
        CorsFilter.apply();

        Gson gson = new Gson();

        //Controllers
        SpotifyAddToPlaylistController spotifyAddToPlaylistController = new SpotifyAddToPlaylistController();
        RecommendationsController recommendationsController = new RecommendationsController();
        SpotifyGetPlaylistController spotifyGetPlaylist = new SpotifyGetPlaylistController();
        SpotifySearchController spotifySearchController = new SpotifySearchController();
        SpotifyAudioFeaturesController audioFeaturesController = new SpotifyAudioFeaturesController();
        PseudoChannelController psuedoChannelController = new PseudoChannelController();
        SpotifyGetAlbumController getAlbumController = new SpotifyGetAlbumController();
        AddToLibraryController addToLibraryController = new AddToLibraryController();
        SRController srController = new SRController();

        //Endpoints relating to spotify
        path("/spotify", () -> {
            //Search through the spotify-API
            post("/search", (request,response) -> {
                response.type("application/json"); //definiera svar som json
                String responseBody = spotifySearchController.search(request.body());
                if(responseBody.contains("statusCode")){
                    int statusCode = Integer.parseInt(responseBody.split(":")[1].substring(0,4));
                    response.status(statusCode);
                } else {
                    response.status(200);
                }
                return responseBody;
            });
            //Add a track to your spotify library
            post("/AddToLibrary", (request,response) -> {
                response.type("application/json"); //definiera svar som json
                String responseBody = addToLibraryController.addToLibrary(request.body());
                if(responseBody.contains("statusCode")){
                    int statusCode = Integer.parseInt(responseBody.split(":")[1].substring(0,4));
                    response.status(statusCode);
                } else {
                    response.status(200);
                }
                return responseBody;
            });
            //Endpoints concerning playlists
            path("/playlist", () -> {
                //Get a list of the users spotify playlists.
                post("/fetch", (request,response) -> {
                    response.type("application/json"); //definiera svar som json
                    String responseBody = spotifyGetPlaylist.getPlayList(request.body());
                    if(responseBody.contains("statusCode")){
                        int statusCode = Integer.parseInt(responseBody.split(":")[1].substring(0,4));
                        response.status(statusCode);
                    } else {
                        response.status(200);
                    }
                    return responseBody;
                });
                //Add a song to a playlist
                post("/add", (request,response) -> {
                    response.type("application/json"); //definiera svar som json
                    String responseBody = spotifyAddToPlaylistController.addToPlayList(request.body());
                    if(responseBody.contains("statusCode")){
                        int statusCode = Integer.parseInt(responseBody.split(":")[1].substring(0,4));
                        response.status(statusCode);
                    } else {
                        response.status(200);
                    }
                    return responseBody;
                });
            });
            //Get album, already converted to JSON
            post("/album", (request, response) -> {
                response.type("application/json"); //definiera svar som json
                String responseBody = getAlbumController.getAlbum(request.body());
                if(responseBody.contains("statusCode")){
                    int statusCode = Integer.parseInt(responseBody.split(":")[1].substring(0,4));
                    response.status(statusCode);
                } else {
                    response.status(200);
                }
                return responseBody;
            });
          
            //MARK: Recommendations
            post("/recommendation", (request, response) -> {
                System.out.println("recommending song");
                response.type("application/json"); //definiera svar som json
                TrackMessage msg = gson.fromJson(request.body(), TrackMessage.class); //hämta json object från body som ett definierat objekt
                System.out.println("auth: " + msg.getAuth());
                System.out.println("trackID " + msg.getTrackID());
                String responseBody = recommendationsController.getRecommendation(msg);
                if(responseBody.contains("statusCode")){
                    int statusCode = Integer.parseInt(responseBody.split(":")[1].substring(0,4));
                    response.status(statusCode);
                } else {
                    response.status(200);
                }
                return responseBody;
            });

            //MARK: AudioFeatures
            post("/audioFeatures", (request, response) -> {
                response.type("application/json"); //definiera svar som json
                TrackMessage msg = gson.fromJson(request.body(), TrackMessage.class); //hämta json object från body som ett definierat objekt
                String responseBody = audioFeaturesController.getAudioFeatures(msg);
                if(responseBody.contains("statusCode")){
                    int statusCode = Integer.parseInt(responseBody.split(":")[1].substring(0,4));
                    response.status(statusCode);
                } else {
                    response.status(200);
                }
                return responseBody;
            });
        });

        path("/SR", () -> {
            //MARK: SR spelas nu
            post("/currentlyPlaying", (request, response) -> {
                response.type("application/json"); //definiera svar som json
                SRMessage msg = gson.fromJson(request.body(), SRMessage.class); //hämta json object från body som ett definierat objekt
                String responseBody = srController.getSongPlaying(msg);
                if(responseBody.contains("statusCode")){
                    int statusCode = Integer.parseInt(responseBody.split(":")[1].substring(0,4));
                    response.status(statusCode);
                } else {
                    response.status(200);
                }
                return responseBody;
            });
        });

        post("/pseudoChannel",((request, response) -> {
            response.type("application/json"); //definiera svar som json
            return psuedoChannelController.getChannel(request.body());
        }), gson :: toJson);


        //This endpoint handles the preflighting automatically done by browsers. Therefore it is not in the documentation.
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
