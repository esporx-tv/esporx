package tv.esporx.scheduling.wrappers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import tv.esporx.scheduling.ChannelResponse;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchTVChannelResponse implements ChannelResponse {

    @JsonProperty("channel_count")
    private String viewerCount;

    @JsonProperty
    private TwitchTVChannel channel;

    @Override
    public String getViewerCount() {
        return viewerCount;
    }

    public void setViewerCount(String channelCount) {
        this.viewerCount = channelCount;
    }

    @Override
    public String getChannelName() {
        return channel.getChannelName();
    }

    public void setChannelName(TwitchTVChannel channelName) {
        this.channel = channelName;
    }


}