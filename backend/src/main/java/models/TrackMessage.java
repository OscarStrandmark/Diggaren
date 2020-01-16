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
    private String auth;
    private String trackID;

    public TrackMessage(String auth, String trackID) {
        this.auth = auth;
        this.trackID = trackID;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getTrackID() {
        return trackID;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }
}
