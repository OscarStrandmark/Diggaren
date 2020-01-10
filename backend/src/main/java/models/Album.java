package models;

public class Album {

    public String name;
    public String[] artists; // Bör vara Array
  //  public String label;
    public String id;
    public String album_type;

    public Album() {
    }

    public Album(String name, String[] artists,  String id, String album_type){
        this.name = name;
        this.artists = artists;
      //  this.label = label;
        this.id = id;
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

//    public String getLabel() {
//        return label;
//    }
//
//    public void setLabel(String label) {
//        this.label = label;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() { return album_type; }

    public void setType(String type) { this.album_type = type; }

}
