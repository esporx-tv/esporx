package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import tv.esporx.domain.Occurrence;

import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;
import static tv.esporx.framework.time.DateTimeUtils.diffInDays;

class TimelineDailyRepeater extends TimelineRepeater {

    public TimelineDailyRepeater(Timeline.Contents contents) {
        super(contents, DAILY);
    }

    protected final void addCopies(Occurrence occurrence, DateTime start, DateTime end) {
        int daysFromOriginal = 1;
        do {
            start = start.plusDays(1);
            this.contents.add(occurrence.copyPlusDays(daysFromOriginal++));
        }
        while(start.plusDays(1).isBefore(end));
    }

    protected final boolean isReplicationNeeded(DateTime start, DateTime end) {
        return diffInDays(start, end) >= 1;
    }

}
