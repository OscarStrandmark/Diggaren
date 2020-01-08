package util;

public class RecommendationMessage {
    private String authorization;
    private String trackID;

    public RecommendationMessage() {
    }

    public RecommendationMessage(String authorization, String trackID) {
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
