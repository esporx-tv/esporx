package tv.esporx.services.timeline;


import org.joda.time.DateTime;
import tv.esporx.domain.Occurrence;

import static tv.esporx.domain.FrequencyType.FrequencyTypes.MONTHLY;

class TimelineMonthlyRepeater extends TimelineRepeater {

    public TimelineMonthlyRepeater(Timeline contents) {
        super(contents, MONTHLY);
    }

    protected final void addCopies(Occurrence occurrence, DateTime start, DateTime end) {
        int fromOriginal = 0;
        do {
            this.contents.add(occurrence.copyPlusMonths(fromOriginal++));
            start = start.plusMonths(1);
        }
        while(start.isBefore(end));
    }

}
