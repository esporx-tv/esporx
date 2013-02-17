package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import tv.esporx.domain.Occurrence;

import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;

class TimelineDailyRepeater extends TimelineRepeater {

    public TimelineDailyRepeater(Timeline contents) {
        super(contents, DAILY);
    }

    protected final void addCopies(Occurrence occurrence, DateTime start, DateTime end) {
        int fromOriginal = 0;
        do {
            this.contents.add(occurrence.copyPlusDays(fromOriginal++));
            start = start.plusDays(1);
        }
        while(start.isBefore(end));
    }

}
