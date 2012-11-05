package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import tv.esporx.domain.Occurrence;

import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;
import static tv.esporx.framework.time.DateTimeUtils.diffInDays;

class TimelineDailyRepeater extends TimelineRepeater {

    public TimelineDailyRepeater(Timeline.Contents contents) {
        super(contents, DAILY);
    }

    protected final void addCopies(Occurrence occurrence, DateTime end, DateTime occurrenceStart) {
        int daysFromOriginal = 1;
        do {
            occurrenceStart = occurrenceStart.plusDays(1);
            this.contents.add(occurrence.copyPlusDays(daysFromOriginal++));
        }
        while(occurrenceStart.plusDays(1).isBefore(end));
    }

    protected final boolean isReplicationNeeded(DateTime start, DateTime end) {
        return diffInDays(start, end) >= 1;
    }

}
