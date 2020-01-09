import com.google.gson.Gson;
import controllers.AudioFeaturesController;
import controllers.RecommendationsController;
import controllers.SRController;
import util.SRMessage;
import util.TrackMessage;

import static spark.Spark.*;

public class StartAPI {

    public static void main(String[] args) {

        Gson gson = new Gson();

        //Controllers
        RecommendationsController recommendationsController = new RecommendationsController();
        AudioFeaturesController audioFeaturesController = new AudioFeaturesController();
        SRController srController = new SRController();

        port(5050);

        Spotify spotify = new Spotify();
        SR sr = new SR();

        get("/",(request,response) -> {
            return sr.getCurrentlyPlaying(request.attribute("channelID"));
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
