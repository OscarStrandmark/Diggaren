

public class StartAPI {

    public static void main(String[] args) {


        Message msg = new Message();
        SpotifyGetAlbum get = new SpotifyGetAlbum();
        port(5050);
        Gson gson = new Gson();

        //Controllers
        SpotifyAddToPlaylistController spotifyAddToPlaylistController = new SpotifyAddToPlaylistController();
        RecommendationsController recommendationsController = new RecommendationsController();
        SpotifyGetPlaylistController spotifyGetPlaylist = new SpotifyGetPlaylistController();
        SpotifySearchController spotifySearchController = new SpotifySearchController();
        AudioFeaturesController audioFeaturesController = new AudioFeaturesController();
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
                return get.getAlbum(msg);
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
            post("/currently_playing", (request, response) -> {
                response.type("application/json"); //definiera svar som json
                SRMessage msg = gson.fromJson(request.body(), SRMessage.class); //hämta json object från body som ett definierat objekt
                return srController.getSongPlaying(msg);
            }, gson :: toJson);
        });

    }
}
