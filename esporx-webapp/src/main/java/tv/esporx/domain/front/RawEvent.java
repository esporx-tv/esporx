package tv.esporx.domain.front;

import org.joda.time.DateTime;
import tv.esporx.domain.FrequencyType;

import java.util.Collection;

import static java.util.Collections.emptyList;

public class RawEvent {
    private String title = "";
    private boolean highlighted;
    private String description = "";
    private Collection<DateTime> startDates;
    private Collection<DateTime> endDates;
    private Collection<FrequencyType> frequencies;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<DateTime> getStartDates() {
        return startDates;
    }

    public void setStartDates(Collection<DateTime> startDates) {
        this.startDates = startDates;
    }

    public Collection<DateTime> getEndDates() {
        return endDates;
    }

    public void setEndDates(Collection<DateTime> endDates) {
        this.endDates = endDates;
    }

    public Collection<FrequencyType> getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(Collection<FrequencyType> frequencies) {
        this.frequencies = frequencies;
    }
}
