import static spark.Spark.port;
import static spark.Spark.get;

public class StartAPI {
    public static void main(String[] args) {
        port(5050);

        Spotify spotify = new Spotify();
        SR sr = new SR();

        get("/",(request,response) -> {
            return sr.getCurrentlyPlaying(request.attribute("channelID"));
        });
    }
}
