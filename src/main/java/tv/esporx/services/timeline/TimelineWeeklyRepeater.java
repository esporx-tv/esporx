package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import tv.esporx.domain.Occurrence;

import static tv.esporx.domain.FrequencyType.FrequencyTypes.WEEKLY;

class TimelineWeeklyRepeater extends TimelineRepeater {

    public TimelineWeeklyRepeater(Timeline contents) {
        super(contents, WEEKLY);
    }

    protected final void addCopies(Occurrence occurrence, DateTime start, DateTime end) {
        int fromOriginal = 0;
        do {
            this.contents.add(occurrence.copyPlusWeeks(fromOriginal++));
            start = start.plusWeeks(1);
        }
        while(start.isBefore(end));
    }
}
