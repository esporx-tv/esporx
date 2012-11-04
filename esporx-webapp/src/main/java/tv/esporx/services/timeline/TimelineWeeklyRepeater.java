package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.esporx.collections.RepeatingOccurrencePredicate;
import tv.esporx.domain.Occurrence;

import static com.google.common.collect.Iterables.filter;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.WEEKLY;
import static tv.esporx.framework.time.DateTimeUtils.diffInWeeks;
import static tv.esporx.framework.time.DateTimeUtils.earliest;

class TimelineWeeklyRepeater {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimelineWeeklyRepeater.class);
    private final Timeline.Contents contents;

    public TimelineWeeklyRepeater(Timeline.Contents contents) {
        this.contents = contents;
    }

    void replicate(Iterable<Occurrence> occurrences, DateTime timelineStart, DateTime timelineEnd) {
        if (isReplicationNeeded(timelineStart, timelineEnd)) {
            LOGGER.info("Replicationg WEEKLY occurrences");
            for (Occurrence occurrence : filter(occurrences, new RepeatingOccurrencePredicate(WEEKLY))) {
                replicateOccurrence(timelineEnd, occurrence);
            }
        }
    }

    private void replicateOccurrence(DateTime timelineEnd, Occurrence occurrence) {
        DateTime end = earliest(occurrence.getEndDateTime(), timelineEnd);
        DateTime occurrenceStart = occurrence.getStartDateTime();
        if (end.isAfter(occurrenceStart) && isReplicationNeeded(occurrenceStart, end)) {
            int weeksFromOriginal = 1;
            do {
                occurrenceStart = occurrenceStart.plusWeeks(1);
                this.contents.add(occurrence.copyPlusWeeks(weeksFromOriginal++));
            }
            while(occurrenceStart.plusWeeks(1).isBefore(timelineEnd));
        }
    }

    private boolean isReplicationNeeded(DateTime start, DateTime end) {
        return diffInWeeks(start, end) >= 1;
    }

}
