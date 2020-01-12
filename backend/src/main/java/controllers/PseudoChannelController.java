package controllers;

import com.google.gson.*;
import models.*;

/**
 * This mess of a class handles the logic behind pseudo-channels.
 *
 * @Author Oscar Strandmark
 */
public class PseudoChannelController {

    //Everything in this method is repeated twice, as it is done for both radio channels.
    public PseudoChannelSelectionResponse getChannel(String json){
        //Convert json to java object.
        Gson gson = new Gson();
        PseudoChannelSelection selection = gson.fromJson(json, PseudoChannelSelection.class);

        SRController srController = new SRController();

        //Get songs currently playing on radio channels
        PlayingSong playingP2 = srController.getSongPlaying(new SRMessage(163));
        PlayingSong playingP3 = srController.getSongPlaying(new SRMessage(164));

        //If at least one station is playing music
        if(playingP2.getPlayingSongName() != null || playingP3.getPlayingSongName() != null){
            SpotifySearchController searchController = new SpotifySearchController();

            //Search the spotify catalog for the songs.
            Search searchP2 = new Search(selection.getAuth(), "track", playingP2.getPlayingSongName());
            Search searchP3 = new Search(selection.getAuth(), "track", playingP3.getPlayingSongName());

            JsonParser parser = new JsonParser();

            boolean successP2 = true;
            boolean successP3 = true;
            String songIdP2 = null;
            String songIdP3 = null;

            //If they exist in the spotify catalog, get their trackID.
            try { //try to get p2
                String searchResultP2 = searchController.search(gson.toJson(searchP2));
                JsonObject trackMapP2 = parser.parse(searchResultP2).getAsJsonObject().get("tracks").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject(); //gets the one and only track from the response as an json object
                songIdP2 = trackMapP2.get("id").getAsString();


            } catch (Exception e){
                successP2 = false;
                e.printStackTrace();
            }

            try { //Try to get p3
                String searchResultP3 = searchController.search(gson.toJson(searchP3));
                JsonObject trackMapP3 = parser.parse(searchResultP3).getAsJsonObject().get("tracks").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject(); //gets the one and only track from the response as an json object
                songIdP3 = trackMapP3.get("id").getAsString();
            } catch (Exception e){
                successP3 = false;
                e.printStackTrace();
            }

            PseudoChannelSelectionResponse response = null;

            //If only one station plays music that is able to be found on spotify, play that one.
            if(successP2 && !successP3){
                response = new PseudoChannelSelectionResponse("P2");
            }

            if(!successP2 && successP3){
                response = new PseudoChannelSelectionResponse("P3");
            }

            //If both stations are playing music found in the spotify catalog.
            if(successP2 && successP3){

                AudioFeaturesController audioFeaturesController = new AudioFeaturesController();

                AudioFeatures featuresP2 = audioFeaturesController.getAudioFeatures(new TrackMessage(selection.getAuth(),songIdP2));
                AudioFeatures featuresP3 = audioFeaturesController.getAudioFeatures(new TrackMessage(selection.getAuth(),songIdP3));

                double valP2 = 0;
                double valP3 = 0;

                //Evalueta their values based on the spotify track analysis.
                switch (selection.getType().toLowerCase()) {
                    case "dance":
                        valP2 = Double.parseDouble(featuresP2.getDanceability());
                        valP3 = Double.parseDouble(featuresP3.getDanceability());
                        break;
                    case "hi_energy":
                        valP2 = Double.parseDouble(featuresP2.getEnergy());
                        valP3 = Double.parseDouble(featuresP3.getEnergy());
                        break;
                    case "lo_energy":
                        //swap variables since we want to compare the opposite
                        valP3 = Double.parseDouble(featuresP2.getEnergy());
                        valP2 = Double.parseDouble(featuresP3.getEnergy());
                        break;
                    case "instrumental":
                        valP2 = Double.parseDouble(featuresP2.getInstrumentalness());
                        valP3 = Double.parseDouble(featuresP3.getInstrumentalness());
                        break;
                    case "hi_tempo":
                        valP2 = Double.parseDouble(featuresP2.getTempo());
                        valP3 = Double.parseDouble(featuresP3.getTempo());
                        break;
                    case "lo_tempo":
                        valP3 = Double.parseDouble(featuresP2.getTempo());
                        valP2 = Double.parseDouble(featuresP3.getTempo());
                        break;
                }
                String result;

                //If value of p2 is higher than p3, play p2. This is why we swap the values for the low_value channels.
                if (valP2 > valP3) {
                    result = "P2";
                } else {
                    result = "P3";
                }
                response = new PseudoChannelSelectionResponse(result);
            }
            return response;
        }
        //If there was an error, or both songs are not playing music: return -1
        return new PseudoChannelSelectionResponse("-1");
    }
}

