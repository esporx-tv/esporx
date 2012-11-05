package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import tv.esporx.domain.Occurrence;

import static tv.esporx.domain.FrequencyType.FrequencyTypes.WEEKLY;
import static tv.esporx.framework.time.DateTimeUtils.diffInWeeks;

class TimelineWeeklyRepeater extends TimelineRepeater {

    public TimelineWeeklyRepeater(Timeline.Contents contents) {
        super(contents, WEEKLY);
    }

    protected final void addCopies(Occurrence occurrence, DateTime end, DateTime occurrenceStart) {
        if (end.isAfter(occurrenceStart) && isReplicationNeeded(occurrenceStart, end)) {
            int weeksFromOriginal = 1;
            do {
                occurrenceStart = occurrenceStart.plusWeeks(1);
                this.contents.add(occurrence.copyPlusWeeks(weeksFromOriginal++));
            }
            while(occurrenceStart.plusWeeks(1).isBefore(end));
        }
    }

    protected final boolean isReplicationNeeded(DateTime start, DateTime end) {
        return diffInWeeks(start, end) >= 1;
    }

}
