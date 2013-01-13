package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import tv.esporx.domain.Occurrence;

import static tv.esporx.domain.FrequencyType.FrequencyTypes.YEARLY;
import static tv.esporx.framework.time.DateTimeUtils.diffInYears;
import static tv.esporx.framework.time.DateTimeUtils.earliest;

class TimelineYearlyRepeater extends TimelineRepeater {

    public TimelineYearlyRepeater(Timeline contents) {
        super(contents, YEARLY);
    }

    protected final void addCopies(Occurrence occurrence, DateTime start, DateTime end) {
        int fromOriginal = 0;
        do {
            this.contents.add(occurrence.copyPlusYears(fromOriginal++));
            start = start.plusYears(1);
        }
        while(start.isBefore(end));
    }

}
