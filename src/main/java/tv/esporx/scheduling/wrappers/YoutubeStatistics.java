package tv.esporx.scheduling.wrappers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeStatistics {

    @JsonProperty("viewCount")
    private long viewerCount;

    public long getViewerCount() {
        return viewerCount;
    }

    public void setViewerCount(long viewerCount) {
        this.viewerCount = viewerCount;
    }
}
