package models;

/**
 * Represents JSON messages sent from client to server
 *
 * Can be used so far to pass messages from client for both
 * Recommendations and AudioFeatures endpoints
 *
 * containing Authorization token and trackID (from Spotify) of the
 * song that will be used as seed for the requested data.
 */
public class TrackMessage {
    private String authorization;
    private String trackID;

    public TrackMessage(String authorization, String trackID) {
        this.authorization = authorization;
        this.trackID = trackID;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getTrackID() {
        return trackID;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }
}
