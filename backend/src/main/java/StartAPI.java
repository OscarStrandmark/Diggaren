import com.google.gson.Gson;
import controllers.RecommendationsController;
import util.RecommendationMessage;

import static spark.Spark.*;

public class StartAPI {

    public static void main(String[] args) {

        //MARK: Testing ----------------------------------------------
        RecommendationsController c = new RecommendationsController();
        c.getRecommendation(new RecommendationMessage("BQCTmYsB3ElYD4gVnmqEzZ_mcQrQYK2e07jyO6yGRAiofEkm2qft1_Xld70o0v-7oY" +
                "_qfRotBm0gyTw2KXC4Qel9Fz2lWF2X8zqf-srlvk_TGRnBbKROLFiBGkr2Vyev5mnYlHQy3JsTHQ",
                "1TKYPzH66GwsqyJFKFkBHQ"));
        //-------------------------------------------------------------

        Gson gson = new Gson();

        RecommendationsController recommendationsController = new RecommendationsController();

        port(5050);

        Spotify spotify = new Spotify();
        SR sr = new SR();

        get("/",(request,response) -> {
            return sr.getCurrentlyPlaying(request.attribute("channelID"));
        });


        post("/recommendations", (request, response) -> {
            response.type("application/json"); //definiera svar som json
            RecommendationMessage msg = gson.fromJson(request.body(), RecommendationMessage.class); //hämta json object från body som ett definierat objekt
            return recommendationsController.getRecommendation(msg);
        }, gson :: toJson);
    }
}
