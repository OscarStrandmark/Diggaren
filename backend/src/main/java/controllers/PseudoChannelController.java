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
    SpotifyAudioFeaturesController audioFeaturesController;
    SpotifySearchController searchController;
    JsonParser parser;
    Gson gson;

    //channel name is not relevant, since we will only return the channelID
    private int[] channels = {132, 163, 164, 213, 223, 205,
            210, 212, 220, 200, 203, 201, 211, 214, 207, 209, 206, 208, 701,
            202, 218, 204, 219, 215, 216, 217, 221, 222, 166, 2576, 2562, 4951, 5283};


    public String getChannel(PseudoChannelSelection selection){
        srController = new SRController();
        audioFeaturesController = new SpotifyAudioFeaturesController();
        searchController = new SpotifySearchController();
        parser = new JsonParser();
        gson = new Gson();

        int choosenChannelID = -1; //channel with highest value
        double choosenChannelValue = -1; //value of the channel with highest value of prefered audiofeature

        for(int i = 0; i < channels.length; i++){
            boolean callFailed = false;
            if(choosenChannelID < 0){ //for initial comparision
                choosenChannelID = channels[i];
                try {

                    String song = srController.getSongPlaying(new SRMessage(channels[i])); //song from SR
                    PlayingSong songObject = gson.fromJson(song,PlayingSong.class);

                    String searchResult = searchController.search(gson.toJson(new Search(selection.getAuth(), "track", songObject.getPlayingSongName()))); //try to get song from spotify

                    JsonObject trackMap = parser.parse(searchResult).getAsJsonObject().get("tracks").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject(); //gets the one and only track from the response as an json object
                    String songId = trackMap.get("id").getAsString();

                    String features = audioFeaturesController.getAudioFeatures(new TrackMessage(selection.getAuth(), songId)); //get audiofeature from Spotify
                    AudioFeatures featuresObject = gson.fromJson(features,AudioFeatures.class);

                    //Only need to check last call for failure, could possibly speed up this endpoint by stopping execution after one call fails.
                    if(features.contains("statusCode")){
                        callFailed = true;
                    }

                    if(!callFailed){
                        switch (selection.getType().toLowerCase()) {
                            case "dance": choosenChannelValue = Double.parseDouble(featuresObject.getDanceability()); break;
                            case "hi_energy": choosenChannelValue = Double.parseDouble(featuresObject.getEnergy()); break;
                            case "lo_energy": choosenChannelValue = 1.0 - Double.parseDouble(featuresObject.getEnergy()); break;
                            case "instrumental": choosenChannelValue = Double.parseDouble(featuresObject.getInstrumentalness()); break;
                            case "hi_tempo": choosenChannelValue = Double.parseDouble(featuresObject.getTempo()); break;
                            case "lo_tempo": choosenChannelValue = 1.0 - Double.parseDouble(featuresObject.getTempo()); break;
                        }
                    }
                } catch (Exception e){
                    choosenChannelValue = 0;
                }
            } else {
                try {
                    double tempValue = 0;
                    String song = srController.getSongPlaying(new SRMessage(channels[i])); //song from SR
                    PlayingSong songObject = gson.fromJson(song,PlayingSong.class);

                    String searchResult = searchController.search(gson.toJson(new Search(selection.getAuth(), "track", gson.fromJson(song,PlayingSong.class).getPlayingSongName()))); //try to get song from spotify

                    JsonObject trackMap = parser.parse(searchResult).getAsJsonObject().get("tracks").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject(); //gets the one and only track from the response as an json object
                    String songId = trackMap.get("id").getAsString();

                    String features = audioFeaturesController.getAudioFeatures(new TrackMessage(selection.getAuth(), songId)); //get audiofeature from Spotify
                    AudioFeatures featuresObject = gson.fromJson(features,AudioFeatures.class);

                    //Only need to check last call for failure, could possibly speed up this endpoint by stopping execution after one call fails.
                    if(features.contains("statusCode")){
                        callFailed = true;
                    }

                    if(!callFailed){
                        switch (selection.getType().toLowerCase()) {
                            case "dance": tempValue = Double.parseDouble(featuresObject.getDanceability()); break;
                            case "hi_energy": tempValue = Double.parseDouble(featuresObject.getEnergy()); break;
                            case "lo_energy": tempValue = 1.0 - Double.parseDouble(featuresObject.getEnergy()); break;
                            case "instrumental": tempValue = Double.parseDouble(featuresObject.getInstrumentalness()); break;
                            case "hi_tempo": tempValue = Double.parseDouble(featuresObject.getTempo()); break;
                            case "lo_tempo": tempValue = 1.0 - Double.parseDouble(featuresObject.getTempo()); break;
                        }
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
        if(choosenChannelID == -1){
            return gson.toJson(new ErrorObject(418));
        }
        return gson.toJson(new PseudoChannelSelectionResponse(choosenChannelID+""));
    }

}

