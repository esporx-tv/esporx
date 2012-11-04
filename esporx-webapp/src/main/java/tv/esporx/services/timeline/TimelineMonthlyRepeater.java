package tv.esporx.services.timeline;


import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.esporx.collections.RepeatingOccurrencePredicate;
import tv.esporx.domain.Occurrence;

import static com.google.common.collect.Iterables.filter;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.MONTHLY;
import static tv.esporx.framework.time.DateTimeUtils.diffInMonths;
import static tv.esporx.framework.time.DateTimeUtils.earliest;

class TimelineMonthlyRepeater {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimelineMonthlyRepeater.class);
    private final Timeline.Contents contents;

    public TimelineMonthlyRepeater(Timeline.Contents contents) {
        this.contents = contents;
    }

    void replicate(Iterable<Occurrence> occurrences, DateTime timelineStart, DateTime timelineEnd) {
        if (isReplicationNeeded(timelineStart, timelineEnd)) {
            LOGGER.info("Replicating MONTHLY occurrences");
            for (Occurrence occurrence : filter(occurrences, new RepeatingOccurrencePredicate(MONTHLY))) {
                replicateOccurrence(timelineEnd, occurrence);
            }
        }
    }

    private void replicateOccurrence(DateTime timelineEnd, Occurrence occurrence) {
        DateTime end = earliest(occurrence.getEndDateTime(), timelineEnd);
        DateTime occurrenceStart = occurrence.getStartDateTime();
        if (end.isAfter(occurrenceStart) && isReplicationNeeded(occurrenceStart, end)) {
            int monthsFromOriginal = 1;
            do {
                occurrenceStart = occurrenceStart.plusMonths(1);
                this.contents.add(occurrence.copyPlusMonths(monthsFromOriginal++));
            }
            while(occurrenceStart.plusMonths(1).isBefore(timelineEnd));
        }
    }

    private boolean isReplicationNeeded(DateTime start, DateTime end) {
        return diffInMonths(start, end) >= 1;
    }

}
