package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import tv.esporx.domain.Occurrence;

import static tv.esporx.domain.FrequencyType.FrequencyTypes.WEEKLY;
import static tv.esporx.framework.time.DateTimeUtils.diffInWeeks;

class TimelineWeeklyRepeater extends TimelineRepeater {

    public TimelineWeeklyRepeater(Timeline.Contents contents) {
        super(contents, WEEKLY);
    }

    protected final void addCopies(Occurrence occurrence, DateTime start, DateTime end) {
        if (end.isAfter(start) && isReplicationNeeded(start, end)) {
            int weeksFromOriginal = 1;
            do {
                start = start.plusWeeks(1);
                this.contents.add(occurrence.copyPlusWeeks(weeksFromOriginal++));
            }
            while(start.plusWeeks(1).isBefore(end));
        }
    }

    protected final boolean isReplicationNeeded(DateTime start, DateTime end) {
        return diffInWeeks(start, end) >= 1;
    }

}
