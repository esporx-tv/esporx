package tv.esporx.scheduling.wrappers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeChannel {

    @JsonProperty("yt$statistics")
    private YoutubeStatistics statistics;

    @JsonProperty("media$group")
    private YoutubeMediaInformation information;

    public long getViewerCount() {
        return statistics.getViewerCount();
    }

    public void setStatistics(YoutubeStatistics statistics) {
        this.statistics = statistics;
    }

    public String getChannelName() {
        return information.getChannelName();
    }

    public void setInformation(YoutubeMediaInformation information) {
        this.information = information;
    }
}
