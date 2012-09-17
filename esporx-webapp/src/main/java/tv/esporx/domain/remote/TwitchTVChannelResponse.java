package tv.esporx.domain.remote;

/*
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
*/

//@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchTVChannelResponse {

    //@JsonProperty("channel_count")
    private String viewerCount;

    //@JsonProperty
    private TwitchTVChannel channel;

    public String getViewerCount() {
        return viewerCount;
    }

    public void setViewerCount(String channelCount) {
        this.viewerCount = channelCount;
    }

    public TwitchTVChannel getChannelName() {
        return channel;
    }

    public void setChannelName(TwitchTVChannel channelName) {
        this.channel = channelName;
    }


}