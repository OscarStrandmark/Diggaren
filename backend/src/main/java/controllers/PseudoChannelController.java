package controllers;

import com.google.gson.*;
import models.*;


/**
 * Controller class for Pseudochannels, handles the logic by incoming message
 * of audio feature preference, returns a suggested channel to listen to based
 * on whats currently playing on the Radio channels.
 */
public class PseudoChannelController {

    private SRController srController;
    AudioFeaturesController audioFeaturesController;
    SpotifySearchController searchController;
    JsonParser parser;
    Gson gson;

    //channel name is not relevant, since we will only return the channelID
    private int[] channels = {132, 163, 164, 213, 223, 205,
            210, 212, 220, 200, 203, 201, 211, 214, 207, 209, 206, 208, 701,
            202, 218, 204, 219, 215, 216, 217, 221, 222, 166, 2576, 2562, 4951, 5283};


    public PseudoChannelSelectionResponse getChannel(PseudoChannelSelection selection){
        srController = new SRController();
        audioFeaturesController = new AudioFeaturesController();
        searchController = new SpotifySearchController();
        parser = new JsonParser();
        gson = new Gson();

        int choosenChannelID = -1; //channel with highest value
        double choosenChannelValue = -1; //value of the channel with highest value of prefered audiofeature

        for(int i = 0; i < channels.length; i++){
            if(choosenChannelID < 0){ //for initial comparision
                choosenChannelID = channels[i];
                try {
                    PlayingSong song = srController.getSongPlaying(new SRMessage(channels[i])); //song from SR
                    String searchResult = searchController.search(gson.toJson(new Search(selection.getAuth(), "track", song.getPlayingSongName()))); //try to get song from spotify
                    JsonObject trackMap = parser.parse(searchResult).getAsJsonObject().get("tracks").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject(); //gets the one and only track from the response as an json object
                    String songId = trackMap.get("id").getAsString();
                    AudioFeatures features = audioFeaturesController.getAudioFeatures(new TrackMessage(selection.getAuth(), songId)); //get audiofeature from Spotify
                    switch (selection.getType().toLowerCase()) {
                        //OBS FIXA LOGIKEN MELLAN HIGH OCH LOW ENERGY, JUST NU MÄTS BARA HÖGSTA ENERGIN OAVSETT OM MAN ÖNSKAR HI ELLER LO, SAMMA MED TEMPO
                        case "dance": choosenChannelValue = Double.parseDouble(features.getDanceability()); break;
                        case "hi_energy": choosenChannelValue = Double.parseDouble(features.getEnergy()); break;
                        case "lo_energy": choosenChannelValue = Double.parseDouble(features.getEnergy()); break; //FIXME
                        case "instrumental": choosenChannelValue = Double.parseDouble(features.getInstrumentalness()); break;
                        case "hi_tempo": choosenChannelValue = Double.parseDouble(features.getTempo()); break;
                        case "lo_tempo": choosenChannelValue = Double.parseDouble(features.getTempo()); break;  //FIXME
                    }
                } catch (Exception e){
                    choosenChannelValue = 0;
                }
            }else{
                try {
                    double tempValue = 0;
                    PlayingSong song = srController.getSongPlaying(new SRMessage(channels[i])); //song from SR
                    String searchResult = searchController.search(gson.toJson(new Search(selection.getAuth(), "track", song.getPlayingSongName()))); //try to get song from spotify
                    JsonObject trackMap = parser.parse(searchResult).getAsJsonObject().get("tracks").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject(); //gets the one and only track from the response as an json object
                    String songId = trackMap.get("id").getAsString();
                    AudioFeatures features = audioFeaturesController.getAudioFeatures(new TrackMessage(selection.getAuth(), songId)); //get audiofeature from Spotify
                    switch (selection.getType().toLowerCase()) {
                        //OBS FIXA LOGIKEN MELLAN HIGH OCH LOW ENERGY, JUST NU MÄTS BARA HÖGSTA ENERGIN OAVSETT OM MAN ÖNSKAR HI ELLER LO, SAMMA MED TEMPO
                        case "dance": tempValue = Double.parseDouble(features.getDanceability()); break;
                        case "hi_energy": tempValue = Double.parseDouble(features.getEnergy()); break;
                        case "lo_energy": tempValue = Double.parseDouble(features.getEnergy()); break; //FIXME
                        case "instrumental": tempValue = Double.parseDouble(features.getInstrumentalness()); break;
                        case "hi_tempo": tempValue = Double.parseDouble(features.getTempo()); break;
                        case "lo_tempo": tempValue = Double.parseDouble(features.getTempo()); break;  //FIXME
                    }
                    if(tempValue > choosenChannelValue){
                        System.out.println("new channel with better feature-mathcing was found: \t old = "+choosenChannelValue+"  new = "+tempValue);
                        choosenChannelValue = tempValue;
                        choosenChannelID = channels[i];
                    }
                } catch (Exception e){
                   //Do nothing I guess
                }
            }
        }
        return new PseudoChannelSelectionResponse(choosenChannelID+"");
    }

}

