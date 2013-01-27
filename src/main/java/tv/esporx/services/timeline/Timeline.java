package tv.esporx.services.timeline;

import com.google.common.collect.Multimap;
import org.joda.time.DateTime;
import tv.esporx.collections.predicates.IsAfterStartHourFilter;
import tv.esporx.collections.predicates.IsHappeningAtDayFilter;
import tv.esporx.collections.predicates.IsRepeatingAtFrequencyFilter;
import tv.esporx.domain.Occurrence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ArrayListMultimap.create;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Maps.*;
import static com.google.common.collect.Multimaps.unmodifiableMultimap;
import static java.util.Collections.unmodifiableMap;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.ONCE;
import static tv.esporx.framework.time.DateTimeUtils.toStartDay;
import static tv.esporx.framework.time.DateTimeUtils.toStartHour;

public class Timeline {

    private final DateTime start;
    private final DateTime end;
    private Map<DateTime, Collection<Occurrence>> contents = newHashMap();

    public Timeline(DateTime start, DateTime end) {
        this.start = start;
        this.end = end;
    }

    public Timeline from(List<Occurrence> occurrences) {
        addOnceOccurrences(occurrences);
        replicateRepeatingOccurrences(occurrences);
        removePastOriginOccurrences();
        return this;
    }

    public Map<DateTime, Collection<Occurrence>> perHourMap() {
        return unmodifiableMap(contents);
    }

    public Multimap<DateTime, Occurrence> perHourMultimap() {
        Multimap<DateTime, Occurrence> unfiltered = create();
        for (DateTime key : contents.keySet()) {
            unfiltered.putAll(key, contents.get(key));
        }
        return unmodifiableMultimap(unfiltered);
    }

    public Map<DateTime, Map<DateTime, Collection<Occurrence>>> perDayAndPerHourMap() {
        Map<DateTime, Collection<Occurrence>> hourSlots = perHourMap();
        Map<DateTime, Map<DateTime, Collection<Occurrence>>> timeline = newTreeMap();
        for(DateTime dayKey = toStartDay(start); dayKey.isBefore(end); dayKey = dayKey.plusDays(1)) {
            Map<DateTime, Collection<Occurrence>> dayOccurrences = filterKeys(hourSlots, new IsHappeningAtDayFilter(dayKey));
            timeline.put(dayKey, dayOccurrences);
        }
        return timeline;
    }

    void add(Occurrence occurrence) {
        checkNotNull(occurrence);
        DateTime matchingSlotTime = toStartHour(new DateTime(occurrence.getStartDate()));
        Collection<Occurrence> occurrences = contents.get(matchingSlotTime);
        if (occurrences == null) {
            occurrences = new ArrayList<Occurrence>();
            contents.put(matchingSlotTime, occurrences);
        }
        occurrences.add(occurrence);
    }

    private void addOnceOccurrences(List<Occurrence> occurrences) {
        for (Occurrence occurrence : filter(occurrences, new IsRepeatingAtFrequencyFilter(ONCE))) {
            add(occurrence);
        }
    }

    private void removePastOriginOccurrences() {
        contents = filterKeys(contents, new IsAfterStartHourFilter(start));
    }

    /**
     * Filters frequency types and replicate matching occurrences accordingly
     */
    private void replicateRepeatingOccurrences(List<Occurrence> occurrences) {
        new TimelineDailyRepeater(this).replicate(occurrences, start, end);
        new TimelineWeeklyRepeater(this).replicate(occurrences, start, end);
        new TimelineMonthlyRepeater(this).replicate(occurrences, start, end);
        new TimelineYearlyRepeater(this).replicate(occurrences, start, end);
    }

}