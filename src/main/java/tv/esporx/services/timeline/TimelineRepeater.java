package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import tv.esporx.collections.predicates.IsRepeatingAtFrequencyFilter;
import tv.esporx.domain.Occurrence;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.filter;
import static org.slf4j.LoggerFactory.getLogger;
import static tv.esporx.domain.FrequencyType.FrequencyTypes;
import static tv.esporx.framework.time.DateTimeUtils.earliest;
import static tv.esporx.framework.time.DateTimeUtils.earliestEnd;

/**
 * Template class for Occurrence repeaters.
 * The basic contract is: iterate over all fetched occurrences and replicate their future
 * appearances within the timeline.
 */
abstract class TimelineRepeater {

    protected static final Logger LOGGER = getLogger(TimelineRepeater.class);
    protected final Timeline contents;
    protected FrequencyTypes frequency;

    public TimelineRepeater(Timeline contents, FrequencyTypes frequency) {
        checkArgument(frequency != FrequencyTypes.ONCE, "ONCE is not allowed");
        this.frequency = frequency;
        this.contents = contents;
    }

    public final void replicate(Iterable<Occurrence> occurrences, DateTime timelineStart, DateTime timelineEnd) {
        LOGGER.info("Replicating "+ frequency.name() +" occurrences");
        for (Occurrence occurrence : filter(occurrences, new IsRepeatingAtFrequencyFilter(frequency))) {
            replicateOccurrence(occurrence, timelineStart, timelineEnd);
        }
    }

    private void replicateOccurrence(Occurrence occurrence, DateTime timelineStart, DateTime timelineEnd) {
        DateTime start = earliest(timelineStart, occurrence.getStartDateTime());
        DateTime end = earliestEnd(occurrence.getEndDateTime(), timelineEnd);
        addCopies(occurrence, start, end);
    }

    protected abstract void addCopies(Occurrence occurrence, DateTime start, DateTime end);

}
