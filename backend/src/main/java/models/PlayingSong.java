package models;

/**
 * Represents the response object to the client from the SR "currentlyPlaying"
 * endpoint. Contaning information about current playing song and time of next
 * song playing. Will be used to send as JSON response to requesting client.
 */
public class PlayingSong {
    private String playingSongName;
    private String playingSongArtist;
    private String nextSongStartTime;

    public PlayingSong(String playingSongName, String playingSongArtist, String nextSongStartTime) {
        this.playingSongName = playingSongName;
        this.playingSongArtist = playingSongArtist;
        this.nextSongStartTime = nextSongStartTime;
    }

    public String getPlayingSongName() {
        return playingSongName;
    }

    public void setPlayingSongName(String playingSongName) {
        this.playingSongName = playingSongName;
    }

    public String getPlayingSongArtist() {
        return playingSongArtist;
    }

    public void setPlayingSongArtist(String playingSongArtist) {
        this.playingSongArtist = playingSongArtist;
    }

    public String getNextSongStartTime() {
        return nextSongStartTime;
    }

    public void setNextSongStartTime(String nextSongStartTime) {
        this.nextSongStartTime = nextSongStartTime;
    }
}
