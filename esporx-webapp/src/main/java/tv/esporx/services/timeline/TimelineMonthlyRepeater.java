package tv.esporx.services.timeline;


import org.joda.time.DateTime;
import tv.esporx.domain.Occurrence;

import static tv.esporx.domain.FrequencyType.FrequencyTypes.MONTHLY;
import static tv.esporx.framework.time.DateTimeUtils.diffInMonths;

class TimelineMonthlyRepeater extends TimelineRepeater {

    public TimelineMonthlyRepeater(Timeline.Contents contents) {
        super(contents, MONTHLY);
    }

    protected final void addCopies(Occurrence occurrence, DateTime end, DateTime occurrenceStart) {
        if (end.isAfter(occurrenceStart) && isReplicationNeeded(occurrenceStart, end)) {
            int monthsFromOriginal = 1;
            do {
                occurrenceStart = occurrenceStart.plusMonths(1);
                this.contents.add(occurrence.copyPlusMonths(monthsFromOriginal++));
            }
            while(occurrenceStart.plusMonths(1).isBefore(end));
        }
    }

    protected final boolean isReplicationNeeded(DateTime start, DateTime end) {
        return diffInMonths(start, end) >= 1;
    }

}
