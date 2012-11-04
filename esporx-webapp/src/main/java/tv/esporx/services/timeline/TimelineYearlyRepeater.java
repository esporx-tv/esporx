package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.esporx.collections.RepeatingOccurrencePredicate;
import tv.esporx.domain.Occurrence;

import static com.google.common.collect.Iterables.filter;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.YEARLY;
import static tv.esporx.framework.time.DateTimeUtils.diffInYears;
import static tv.esporx.framework.time.DateTimeUtils.earliest;

class TimelineYearlyRepeater {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimelineYearlyRepeater.class);
    private final Timeline.Contents contents;

    public TimelineYearlyRepeater(Timeline.Contents contents) {
        this.contents = contents;
    }

    void replicate(Iterable<Occurrence> occurrences, DateTime timelineStart, DateTime timelineEnd) {
        if(isReplicationNeeded(timelineStart, timelineEnd)) {
            LOGGER.info("Replicating YEARLY occurrences");
            for (Occurrence occurrence : filter(occurrences, new RepeatingOccurrencePredicate(YEARLY))) {
                replicateOccurrence(timelineEnd, occurrence);
            }
        }
    }

    private void replicateOccurrence(DateTime timelineEnd, Occurrence occurrence) {
        DateTime end = earliest(occurrence.getEndDateTime(), timelineEnd);
        DateTime occurrenceStart = occurrence.getStartDateTime();
        if (end.isAfter(occurrenceStart) && isReplicationNeeded(occurrenceStart, end)) {
            int plusYears = 1;
            do {
                occurrenceStart = occurrenceStart.plusYears(1);
                this.contents.add(occurrence.copyPlusYears(plusYears++));
            }
            while(occurrenceStart.plusYears(1).isBefore(timelineEnd));
        }
    }

    private boolean isReplicationNeeded(DateTime timelineStart, DateTime timelineEnd) {
        return diffInYears(timelineStart, timelineEnd) >= 1;
    }

}
