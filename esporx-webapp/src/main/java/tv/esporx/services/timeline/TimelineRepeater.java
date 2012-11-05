package tv.esporx.services.timeline;

import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.esporx.collections.RepeatingOccurrencePredicate;
import tv.esporx.domain.Occurrence;

import static com.google.common.collect.Iterables.filter;
import static tv.esporx.domain.FrequencyType.FrequencyTypes;
import static tv.esporx.framework.time.DateTimeUtils.earliestEnd;
import static tv.esporx.framework.time.DateTimeUtils.latestBeginning;

/**
 * Template class for Occurrence repeaters.
 * The basic contract is: iterate over all fetched occurrences and replicate their future
 * appearances within the timeline.
 */
abstract class TimelineRepeater {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TimelineDailyRepeater.class);
    protected final Timeline.Contents contents;
    protected FrequencyTypes frequency;

    public TimelineRepeater(Timeline.Contents contents, FrequencyTypes frequency) {
        Preconditions.checkArgument(frequency != FrequencyTypes.ONCE, "ONCE is not allowed");
        this.frequency = frequency;
        this.contents = contents;
    }

    public final void replicate(Iterable<Occurrence> occurrences, DateTime timelineStart, DateTime timelineEnd) {
        if (isReplicationNeeded(timelineStart, timelineEnd)) {
            LOGGER.info("Replicating "+ frequency.name() +" occurrences");
            for (Occurrence occurrence : filter(occurrences, new RepeatingOccurrencePredicate(frequency))) {
                replicateOccurrence(timelineStart, timelineEnd, occurrence);
            }
        }
    }

    private void replicateOccurrence(DateTime timelineStart, DateTime timelineEnd, Occurrence occurrence) {
        DateTime end = earliestEnd(occurrence.getEndDateTime(), timelineEnd);
        DateTime occurrenceStart = latestBeginning(timelineStart, occurrence.getStartDateTime());
        if (end.isAfter(occurrenceStart) && isReplicationNeeded(occurrenceStart, end)) {
            addCopies(occurrence, end, occurrenceStart);
        }
    }

    protected abstract void addCopies(Occurrence occurrence, DateTime end, DateTime occurrenceStart);

    protected abstract boolean isReplicationNeeded(DateTime start, DateTime end);
}
