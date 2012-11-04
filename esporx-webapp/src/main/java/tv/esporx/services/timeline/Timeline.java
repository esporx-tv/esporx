package tv.esporx.services.timeline;

import com.google.common.collect.MapMaker;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tv.esporx.collections.ByHourOccurrenceIndexer;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.OccurrenceRepository;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Multimaps.index;
import static java.util.Collections.unmodifiableMap;
import static tv.esporx.domain.Occurrence.BY_ASCENDING_START_DATE;
import static tv.esporx.framework.time.DateTimeUtils.*;

@Component
public class Timeline {

    public static final int MAX_MONTHS_AROUND_TODAY = 2;

    static class Contents {
        private ConcurrentMap<DateTime, Collection<Occurrence>> contents = new MapMaker().weakValues().makeMap();

        Map<DateTime, Collection<Occurrence>> asMap() {
            return unmodifiableMap(contents);
        }

        void initialize(List<Occurrence> occurrences, DateTime timelineStart, DateTime timelineEnd) {
            contents.clear();
            contents.putAll(index(occurrences, new ByHourOccurrenceIndexer()).asMap());
            replicateRepeatingOccurrences(occurrences, timelineStart, timelineEnd);
        }

        void add(Occurrence occurrence) {
            DateTime matchingSlotTime = toStartHour(new DateTime(occurrence.getStartDate()));
            Collection<Occurrence> occurrences = contents.get(matchingSlotTime);
            if (occurrences == null) {
                occurrences = new ArrayList<Occurrence>();
                contents.put(matchingSlotTime, occurrences);
            }
            occurrences.add(occurrence);
        }

        /**
         * Filters frequency types and replicate matching occurrences accordingly
         */
        private void replicateRepeatingOccurrences(List<Occurrence> occurrences, DateTime timelineStart, DateTime timelineEnd) {
            new TimelineDailyRepeater(this).replicate(occurrences, timelineStart, timelineEnd);
            new TimelineWeeklyRepeater(this).replicate(occurrences, timelineStart, timelineEnd);
            new TimelineMonthlyRepeater(this).replicate(occurrences, timelineStart, timelineEnd);
            new TimelineYearlyRepeater(this).replicate(occurrences, timelineStart, timelineEnd);
        }
    }

    private final OccurrenceRepository occurrenceRepository;
    private final Contents contents;

    @Autowired
    public Timeline(OccurrenceRepository occurrenceRepository) {
        this.occurrenceRepository = occurrenceRepository;
        contents = new Contents();
    }

    public void update(DateTime startDay, DateTime endDay) {
        checkSanity(startDay, endDay);

        contents.initialize(occurrenceRepository.findAllInRange(toStartDay(startDay), toEndDay(endDay)), startDay, endDay);
    }

    public Set<Occurrence> get(DateTime hour) {
        return sorted(contents.asMap().get(toStartHour(hour)));
    }

    Set<Occurrence> getAll() {
        return sorted(concat(contents.asMap().values()));
    }

    private Set<Occurrence> sorted(Iterable<Occurrence> rawOccurrences) {
        Set<Occurrence> occurrences = new TreeSet<Occurrence>(BY_ASCENDING_START_DATE);
        occurrences.addAll(newArrayList(rawOccurrences));
        return occurrences;
    }

    private void checkSanity(DateTime startDay, DateTime endDay) {
        checkNotNull(startDay);
        checkNotNull(endDay);
        checkArgument(startDay.isBefore(endDay));
        checkRange(startDay, endDay);
    }

    private static void checkRange(DateTime startDay, DateTime endDay) {
        DateTime now = new DateTime();
        checkArgument(diffInMonths(startDay, now) < MAX_MONTHS_AROUND_TODAY);
        checkArgument(diffInMonths(endDay, now) < MAX_MONTHS_AROUND_TODAY);
    }
}
