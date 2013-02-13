package tv.esporx.scheduling.wrappers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import tv.esporx.scheduling.ChannelResponse;


@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeChannelResponse implements ChannelResponse {

    @JsonProperty("entry")
    private YoutubeChannel channel;

    @Override
    public String getViewerCount() {
        return String.valueOf(channel.getViewerCount());
    }

    @Override
    public String getChannelName() {
        return channel.getChannelName();
    }
}
