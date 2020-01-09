package models;

public class Message {

    public String authorization = "BQBT4gfiQpeTUsxYIUVgLofhcLv2VDBTXt2f2WGi7S798taXaQBssTPTH9izypOmtFB-pLRVSKADIXPc5EfiEtz9Ei4iZ43EcDnWLCH3HahrhLnQZ7pCbF5fB5aKMXMIFfDVBq0Yt9-6LV6KyUsXVxwJllsLxHDbhPafWuqubm7aiUm_MiEBz6n8mz6Nf4Wo_Lkwix4EcQh0rilRv81jRJJRTuBOQ-cVPgus2QExWLGylsdZhxoNzqgi9pxQLaJhsax_swQENbg";
    public String albumID = "0sNOF9WDwhWunNAHPD3Baj";

    public Message() {

    }

    public Message(String authorization, String trackID){
        this.authorization = authorization;
        this.albumID = trackID;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getTrackID() {
        return albumID;
    }

    public void setTrackID(String trackId) {
        this.albumID = trackId;
    }
}
