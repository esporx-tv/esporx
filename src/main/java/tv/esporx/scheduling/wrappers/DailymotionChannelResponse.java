package tv.esporx.scheduling.wrappers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import tv.esporx.scheduling.ChannelResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DailymotionChannelResponse implements ChannelResponse {

    @JsonProperty("id")
    private String channelName;

    @JsonProperty("views_last_hour")
    private String viewerCount;

    @Override
    public String getChannelName() {
        return channelName;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String getViewerCount() {
        return viewerCount;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setViewerCount(String viewerCount) {
        this.viewerCount = viewerCount;
    }
}
