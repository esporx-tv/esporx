package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import tv.esporx.domain.Occurrence;

import static tv.esporx.domain.FrequencyType.FrequencyTypes.YEARLY;
import static tv.esporx.framework.time.DateTimeUtils.diffInYears;

class TimelineYearlyRepeater extends TimelineRepeater {

    public TimelineYearlyRepeater(Timeline.Contents contents) {
        super(contents, YEARLY);
    }

    protected final void addCopies(Occurrence occurrence, DateTime start, DateTime end) {
        if (end.isAfter(start) && isReplicationNeeded(start, end)) {
            int plusYears = 1;
            do {
                start = start.plusYears(1);
                this.contents.add(occurrence.copyPlusYears(plusYears++));
            }
            while(start.plusYears(1).isBefore(end));
        }
    }

    protected final boolean isReplicationNeeded(DateTime timelineStart, DateTime timelineEnd) {
        return diffInYears(timelineStart, timelineEnd) >= 1;
    }

}
