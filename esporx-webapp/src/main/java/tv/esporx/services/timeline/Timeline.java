package tv.esporx.services.timeline;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.ArrayListMultimap.create;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.filterKeys;
import static com.google.common.collect.Maps.transformValues;
import static com.google.common.collect.Multimaps.filterValues;
import static com.google.common.collect.Multimaps.index;
import static com.google.common.collect.Sets.filter;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static com.google.common.collect.Sets.newTreeSet;
import static java.util.Collections.unmodifiableMap;
import static tv.esporx.domain.Occurrence.BY_ASCENDING_START_DATE;
import static tv.esporx.framework.time.DateTimeUtils.diffInMonths;
import static tv.esporx.framework.time.DateTimeUtils.signedDiffInHours;
import static tv.esporx.framework.time.DateTimeUtils.toEndDay;
import static tv.esporx.framework.time.DateTimeUtils.toStartDay;
import static tv.esporx.framework.time.DateTimeUtils.toStartHour;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tv.esporx.collections.comparators.ChannelByMaxViewerComparator;
import tv.esporx.collections.functions.ByHourOccurrenceIndexer;
import tv.esporx.collections.predicates.IsAfterStartHourFilter;
import tv.esporx.collections.predicates.IsDerivedOccurrenceFilter;
import tv.esporx.collections.predicates.IsLiveOccurrenceFilter;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.OccurrenceRepository;

import com.google.common.base.Function;

@Component
public class Timeline {

    private int maxMonthsAroundToday = 2;


    static class Contents {

        private ConcurrentMap<DateTime, Collection<Occurrence>> contents = new MapMaker().weakValues().makeMap();

        void initialize(List<Occurrence> occurrences, DateTime timelineStart, DateTime timelineEnd) {
            reinitializeCache(index(occurrences, new ByHourOccurrenceIndexer()).asMap());
            replicateRepeatingOccurrences(occurrences, timelineStart, timelineEnd);
            removePastOriginOccurrences(timelineStart);
        }

        void add(Occurrence occurrence) {
            checkNotNull(occurrence);
            delete(occurrence);
            DateTime matchingSlotTime = toStartHour(new DateTime(occurrence.getStartDate()));
            Collection<Occurrence> occurrences = contents.get(matchingSlotTime);
            if (occurrences == null) {
                occurrences = new ArrayList<Occurrence>();
                contents.put(matchingSlotTime, occurrences);
            }
            occurrences.add(occurrence);
        }

        void delete(Occurrence occurrence) {
            checkNotNull(occurrence);
            reinitializeCache(filterValues(asMultimap(contents), not(new IsDerivedOccurrenceFilter(occurrence))).asMap());
        }

        private void reinitializeCache(Map<DateTime, Collection<Occurrence>> values) {
            contents.clear();
            contents.putAll(values);
        }

        Map<DateTime, Collection<Occurrence>> asMap() {
            return unmodifiableMap(contents);
        }

        private Multimap<DateTime, Occurrence> asMultimap(Map<DateTime, Collection<Occurrence>> contents) {
            Multimap<DateTime, Occurrence> unfiltered = ArrayListMultimap.create();
            for(DateTime key : contents.keySet()) {
                unfiltered.putAll(key, contents.get(key));
            }
            return unfiltered;
        }

        private void removePastOriginOccurrences(final DateTime timelineStart) {
            contents = new ConcurrentHashMap<DateTime, Collection<Occurrence>>(filterKeys(contents, new IsAfterStartHourFilter(timelineStart)));
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

    public void addOrUpdate(Occurrence occurrence) {
        contents.add(occurrence);
    }

    public void delete(Occurrence occurrence) {
        contents.delete(occurrence);
    }

    public Set<Occurrence> occurrencesAt(DateTime hour) {
        return sorted(contents.asMap().get(toStartHour(hour)));
    }

    public Set<Occurrence> occurrencesBetween(DateTime start, DateTime end) {
        checkSanity(start, end);
        return (signedDiffInHours(start, end) == 0) ? occurrencesAt(start) : iterate(start, end);
    }

    public Map<DateTime, Collection<Occurrence>> occurrencesPerHoursAt(DateTime start, DateTime end, int limit) {
    	checkSanity(start, end);
        int inserted = 0;
        Multimap<DateTime, Occurrence> sortedOccurrences = TreeMultimap.<DateTime, Occurrence>create();
        DateTime current = toStartHour(start);
        do {
            SortedSet<Occurrence> filtered = hourFilteredOccurrences(current);
            if (filtered.size() > limit - inserted) {
                filtered = subset(filtered, limit - inserted);
            }
            inserted = filtered.size();
            sortedOccurrences.putAll(current, filtered);
        	current = current.plusHours(1);
        } while (inserted <= limit && current.isBefore(end.plusMillis(1)));


        return sortedOccurrences.asMap();
    }

    private SortedSet<Occurrence> subset(SortedSet<Occurrence> filtered, int nFirst) {
        SortedSet<Occurrence> occurrences = newTreeSet();
        Iterator<Occurrence> iterator = filtered.iterator();
        for(int i = 0; i < nFirst && iterator.hasNext(); i++) {
            occurrences.add(iterator.next());
        }
        return occurrences;
    }


    public Map<DateTime, Map<DateTime, Collection<Occurrence>>> occurrencesPerDaysAt(DateTime start, DateTime end) {
        checkSanity(start, end);
        DateTime current = start;

        Map<DateTime, Multimap<DateTime, Occurrence>> result = new TreeMap<DateTime, Multimap<DateTime, Occurrence>>();
        do {
            DateTime startDayCurrent = toStartDay(current);

            Multimap<DateTime, Occurrence> occurrenceMultimap = result.get(startDayCurrent);
            Multimap<DateTime, Occurrence> multimap = hourOccurrencesAsMultimap(current);
            if (occurrenceMultimap == null) {
                result.put(startDayCurrent, multimap);
            } else if (!multimap.isEmpty()) {
                occurrenceMultimap.putAll(multimap);
                result.put(startDayCurrent, occurrenceMultimap);
            }

            current = current.plusHours(1);
        } while (current.isBefore(end.plusMillis(1)));

        return transformValues(result, new Function<Multimap<DateTime, Occurrence>, Map<DateTime, Collection<Occurrence>>>() {
            @Override
            public Map<DateTime, Collection<Occurrence>> apply(Multimap<DateTime, Occurrence> occurrencesPerHour) {
            return occurrencesPerHour.asMap();
            }
        });
    }

    public int getMaxMonthsAroundToday() {
        return maxMonthsAroundToday;
    }

    /*test-only*/ void setMaxMonthsAroundToday(int maxMonthsAroundToday) {
        this.maxMonthsAroundToday = maxMonthsAroundToday;
    }

    /*test-only*/ Set<Occurrence> allOccurrences() {
        return sorted(concat(contents.asMap().values()));
    }

    /*test-only*/ Contents getContents() {
        return contents;
    }

    private Set<Occurrence> iterate(DateTime start, DateTime end) {
        Set<Occurrence> occurrences = newLinkedHashSet();
        DateTime current = start;
        while (current.isBefore(end)) {
            occurrences.addAll(occurrencesAt(current));
            current = current.plusHours(1);
        }
        return occurrences;
    }

    private Set<Occurrence> sorted(Iterable<Occurrence> rawOccurrences) {
        Set<Occurrence> occurrences = new TreeSet<Occurrence>(BY_ASCENDING_START_DATE);
        if(rawOccurrences != null) {
            occurrences.addAll(newArrayList(rawOccurrences));
        }
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
                "Start [" + startDay + "] should be at most " + maxMonthsAroundToday +
                        " months before today at 00:00:00:000000");
        checkArgument(diffInMonths(endDay, toEndDay(now)) < maxMonthsAroundToday,
                "End ["+startDay+"] should be at most " + maxMonthsAroundToday+
                        " months after today, 23:59:59:999999");
    }

    private Multimap<DateTime, Occurrence> hourOccurrencesAsMultimap(DateTime current) {
        Multimap<DateTime, Occurrence> occurrencesPerHour = create();
        occurrencesPerHour.putAll(toStartHour(current), occurrencesAt(current));
        return occurrencesPerHour;
    }
    
    /**
     *  Return occurrences at the start of the hour
     */
    private SortedSet<Occurrence> hourFilteredOccurrences(DateTime current) {
    	SortedSet<Occurrence> filteredLiveOccurrences = filteredLiveOccurrence(occurrencesAt(current));
        sortChannelsByViewerCountDesc(filteredLiveOccurrences);
        return filteredLiveOccurrences;
	}

    private void sortChannelsByViewerCountDesc(Set<Occurrence> filteredLiveOccurrences) {
        for (Occurrence occurrence : filteredLiveOccurrences) {
            Set<Channel> sortedChannels = new TreeSet<Channel>(new ChannelByMaxViewerComparator());
            sortedChannels.addAll(occurrence.getChannels());
            occurrence.setChannels(sortedChannels);
        }
    }

    /**
     * Return filtered set of live occurrences and ordered channels for each occurrence
     */
    private SortedSet<Occurrence> filteredLiveOccurrence(Set<Occurrence> occurrences) {
        return newTreeSet(filter(occurrences,                                   //
                new IsLiveOccurrenceFilter()                                    //
        ));
    }

}
