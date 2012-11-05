package tv.esporx.services.timeline;

import com.google.common.base.Predicate;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tv.esporx.collections.ByHourOccurrenceIndexer;
import tv.esporx.collections.FilterCachedOccurrencesPredicate;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.OccurrenceRepository;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.filterKeys;
import static com.google.common.collect.Multimaps.index;
import static java.util.Collections.unmodifiableMap;
import static tv.esporx.domain.Occurrence.BY_ASCENDING_START_DATE;
import static tv.esporx.framework.time.DateTimeUtils.*;

@Component
public class Timeline {

    private int maxMonthsAroundToday = 2;

    static class Contents {
        private ConcurrentMap<DateTime, Collection<Occurrence>> contents = new MapMaker().weakValues().makeMap();

        Map<DateTime, Collection<Occurrence>> asMap() {
            return unmodifiableMap(contents);
        }

        /**
         *
         */
        void initialize(List<Occurrence> occurrences, DateTime timelineStart, DateTime timelineEnd) {
            contents.clear();
            contents.putAll(index(occurrences, new ByHourOccurrenceIndexer()).asMap());
            replicateRepeatingOccurrences(occurrences, timelineStart, timelineEnd);
            removePastOriginOccurrences(timelineStart);
        }

        private void removePastOriginOccurrences(final DateTime timelineStart) {
            contents = new ConcurrentHashMap<DateTime, Collection<Occurrence>>(filterKeys(contents, new FilterCachedOccurrencesPredicate(timelineStart)));
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

    public void update(DateTime start, DateTime end) {
        checkSanity(start, end);
        contents.initialize(occurrenceRepository.findAllInRange(start, end), start, end);
    }

    public Set<Occurrence> occurrencesAt(DateTime hour) {
        return sorted(contents.asMap().get(toStartHour(hour)));
    }

    public int getMaxMonthsAroundToday() {
        return maxMonthsAroundToday;
    }

    void setMaxMonthsAroundToday(int maxMonthsAroundToday) {
        this.maxMonthsAroundToday = maxMonthsAroundToday;
    }

    Set<Occurrence> allOccurrences() {
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

    private void checkRange(DateTime startDay, DateTime endDay) {
        DateTime now = new DateTime();
        checkArgument(diffInMonths(startDay, toStartDay(now)) < maxMonthsAroundToday,
                "Start ["+startDay+"] should be at most " + maxMonthsAroundToday+
                        " before today at 00:00:00:000000");
        checkArgument(diffInMonths(endDay, toEndDay(now)) < maxMonthsAroundToday,
                "End ["+startDay+"] should be at most " + maxMonthsAroundToday+
                        " after today, 23:59:59:999999");
    }
}
