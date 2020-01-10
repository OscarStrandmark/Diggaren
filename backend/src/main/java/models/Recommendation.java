package models;

public class Recommendation {
    private String trackName;
    private String trackID;
    private String artistName;
    private String artistId;

    public Recommendation(String trackName, String trackID, String artistName, String artistId) {
        this.trackName = trackName;
        this.trackID = trackID;
        this.artistName = artistName;
        this.artistId = artistId;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackID() {
        return trackID;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }
}
