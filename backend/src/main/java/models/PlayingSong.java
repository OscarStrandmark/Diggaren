package models;

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
