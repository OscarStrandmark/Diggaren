package models;

/**
 * Class used to handle dataconversion from json to java object.
 *
 * @Author Oscar Strandmark
 */
public class AddToPlaylist {
    private String playlist_id;
    private String auth;
    private String track_id;

    public String getPlaylist_id() {
        return playlist_id;
    }

    public String getAuth() { return auth; }

    public String getTrack_id() {
        return track_id;
    }
}
