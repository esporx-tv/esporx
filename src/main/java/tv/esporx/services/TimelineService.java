package tv.esporx.services;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tv.esporx.collections.comparators.OccurrenceByStartDateComparator;
import tv.esporx.collections.predicates.BroadcastWithChannelPredicate;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Event;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.OccurrenceRepository;
import tv.esporx.services.timeline.Timeline;

import java.util.Collection;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Lists.newArrayList;
import static tv.esporx.framework.time.DateTimeUtils.toStartHour;

@Service
@Transactional
public class TimelineService {

    @Autowired
    private OccurrenceRepository occurrenceRepository;

    @Transactional(readOnly = true)
    public Timeline getTimeline(DateTime start, DateTime end) {
        checkSanity(start, end);
        return new Timeline(start, end).from(occurrenceRepository.findAllInRange(start, end));
    }

    @Transactional(readOnly = true)
    public Occurrence findCurrentBroadcastByChannel(final Channel channel) {
        DateTime startDate = toStartHour(new DateTime());
        Collection<Occurrence> occurrences = getOccurrences(startDate, startDate.plusHours(1));
        try {
            return find(occurrences, new BroadcastWithChannelPredicate(channel));
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public Collection<Occurrence> findNextBroadcastsByEvent(final Event event) {
        DateTime start = toStartHour(new DateTime());
        Collection<Occurrence> occurrences = this.getOccurrences(start, start.plusMonths(1));
        TreeSet<Occurrence> sortedOccurrences = Sets.newTreeSet(new OccurrenceByStartDateComparator());
        sortedOccurrences.addAll(newArrayList(filter(occurrences, new Predicate<Occurrence>() {
            @Override
            public boolean apply(Occurrence input) {
                return input.getEvent().equals(event);
            }
        })));
        return retainFirst(sortedOccurrences);
    }

    @Transactional(readOnly = true)
    public Collection<Occurrence> findNextBroadcastsByChannel(final Channel channel) {
        DateTime start = toStartHour(new DateTime());
        Collection<Occurrence> occurrences = this.getOccurrences(start, start.plusMonths(1));
        NavigableSet<Occurrence> sortedOccurrences = Sets.newTreeSet(new OccurrenceByStartDateComparator());
        sortedOccurrences.addAll(newArrayList(filter(occurrences, new BroadcastWithChannelPredicate(channel))));
        return retainFirst(sortedOccurrences);
    }

    private Collection<Occurrence> retainFirst(NavigableSet<Occurrence> sortedOccurrences) {
        Collection<Occurrence> result = Lists.newArrayList();
        for (int i = 0; i < sortedOccurrences.size() && i < 10; i++) {
            result.add(sortedOccurrences.pollFirst());
        }
        return result;
    }

    private Collection<Occurrence> getOccurrences(DateTime startDate, DateTime endDate) {
        Timeline timeline = this.getTimeline(startDate, endDate);
        return timeline.perHourMultimap().values();
    }

    private void checkSanity(DateTime startDay, DateTime endDay) {
        checkNotNull(startDay);
        checkNotNull(endDay);
        checkArgument(startDay.isBefore(endDay));
    }

}
