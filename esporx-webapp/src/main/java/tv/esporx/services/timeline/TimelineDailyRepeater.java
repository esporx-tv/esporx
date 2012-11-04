package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.esporx.collections.RepeatingOccurrencePredicate;
import tv.esporx.domain.Occurrence;

import static com.google.common.collect.Iterables.filter;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;
import static tv.esporx.framework.time.DateTimeUtils.diffInDays;
import static tv.esporx.framework.time.DateTimeUtils.earliest;

class TimelineDailyRepeater {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimelineDailyRepeater.class);
    private final Timeline.Contents contents;

    public TimelineDailyRepeater(Timeline.Contents contents) {
        this.contents = contents;
    }

    void replicate(Iterable<Occurrence> occurrences, DateTime timelineStart, DateTime timelineEnd) {
        if (isReplicationNeeded(timelineStart, timelineEnd)) {
            LOGGER.info("Replicating DAILY occurrences");
            for (Occurrence occurrence : filter(occurrences, new RepeatingOccurrencePredicate(DAILY))) {
                replicateOccurrence(timelineEnd, occurrence);
            }
        }
    }

    private void replicateOccurrence(DateTime timelineEnd, Occurrence occurrence) {
        DateTime end = earliest(occurrence.getEndDateTime(), timelineEnd);
        DateTime occurrenceStart = occurrence.getStartDateTime();
        if (end.isAfter(occurrenceStart) && isReplicationNeeded(occurrenceStart, end)) {
            int daysFromOriginal = 1;
            do {
                occurrenceStart = occurrenceStart.plusDays(1);
                this.contents.add(occurrence.copyPlusDays(daysFromOriginal++));
            }
            while(occurrenceStart.plusDays(1).isBefore(end));
        }
    }

    private boolean isReplicationNeeded(DateTime start, DateTime end) {
        return diffInDays(start, end) >= 1;
    }

}
