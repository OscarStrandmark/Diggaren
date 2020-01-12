package models;

/**
 * Class used to handle dataconversion from json to java object.
 *
 * @Author Oscar Strandmark
 */
public class PseudoChannelSelectionResponse {
    private String channel;

    public PseudoChannelSelectionResponse(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }
}
