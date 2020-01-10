package models;

public class Message {

    public String authorization = "BQAksoscVtPhvGqXd7RCvbhNN87en-9H6t7vuW5i1N42aAFK1zTGZ4k-_veCTPOdR4vlzwQc1ix7pVUXsIOG0gxorSxgczuRIp0O73MWIf0NGHcInlrUyvnA6WdX6478QM3oim438q2bxMHWnYvOtL0P6-xeaDIgUydj1pFXepyw_u-AibWcYHDZQEGnRc5u5qUIb_ZFRqqlV0iyjKs-5VmN1KBtK_aKdjqOhrfDHEtxzmdtQZ7ykxmFEXTsBY8oAtAP2SU2_k4";
    public String trackID = "28iw1FqJf6Vnfl2Fcu9rBh";

    public Message() {

    }

    public Message(String authorization, String trackID){
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

    public void setTrackID(String trackId) {
        this.trackID = trackId;
    }

}
