package models;

/**
 * Represents the message sent (in JSON) from client
 * to the endpoint. Contains channelID for requested channel
 */
public class SRMessage {
    private int channelID;

    public SRMessage(int channelID) {
        this.channelID = channelID;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }
}
