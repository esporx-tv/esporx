package tv.esporx.scheduling.wrappers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeMediaInformation {

    @JsonProperty("yt$videoid")
    private YoutubeVideoId channelVideoId;

    public void setChannelVideoId(YoutubeVideoId channelVideoId) {
        this.channelVideoId = channelVideoId;
    }

    public String getChannelName() {
        return channelVideoId.getChannelName();
    }
}
