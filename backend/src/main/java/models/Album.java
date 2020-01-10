package models;

public class Album {

    private String name;
    private String[] artists;
    private String album_id;
    private String album_type;

    public Album(String name, String[] artists,  String id, String album_type){
        this.name = name;
        this.artists = artists;
        this.album_id = id;
        this.album_type = album_type;
    }

    public String getName() { return name;  }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtists() {
        String artistsAsString = "";
        for(int i = 0; i<artists.length; i++) {
            artistsAsString += artists[i];
            if(i != artists.length-1){
                artistsAsString += ", ";
            }
        }
        return artistsAsString;
    }

    public void setArtists(String[] artists) {
        this.artists = artists;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getType() { return album_type; }

    public void setType(String type) { this.album_type = type; }

}
