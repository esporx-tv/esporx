package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.esporx.collections.predicates.RepeatingOccurrence;
import tv.esporx.domain.Occurrence;

import static com.google.common.base.Preconditions.checkArgument;
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
        checkArgument(frequency != FrequencyTypes.ONCE, "ONCE is not allowed");
        this.frequency = frequency;
        this.contents = contents;
    }

    public final void replicate(Iterable<Occurrence> occurrences, DateTime timelineStart, DateTime timelineEnd) {
        if (isReplicationNeeded(timelineStart, timelineEnd)) {
            LOGGER.info("Replicating "+ frequency.name() +" occurrences");
            for (Occurrence occurrence : filter(occurrences, new RepeatingOccurrence(frequency))) {
                replicateOccurrence(occurrence, timelineStart, timelineEnd);
            }
        }
    }

    private void replicateOccurrence(Occurrence occurrence, DateTime timelineStart, DateTime timelineEnd) {
        DateTime end = earliestEnd(occurrence.getEndDateTime(), timelineEnd);
        DateTime start = latestBeginning(timelineStart, occurrence.getStartDateTime());
        if (end.isAfter(start) && isReplicationNeeded(start, end)) {
            addCopies(occurrence, start, end);
        }
    }

    protected abstract void addCopies(Occurrence occurrence, DateTime start, DateTime end);

    protected abstract boolean isReplicationNeeded(DateTime start, DateTime end);
}
