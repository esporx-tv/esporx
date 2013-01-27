package tv.esporx.services;

import com.google.common.collect.Multimap;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tv.esporx.collections.comparators.OccurrenceByMaxViewerComparator;
import tv.esporx.collections.predicates.IsLiveOccurrenceFilter;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Event;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.EventRepository;

import java.util.*;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Multimaps.filterValues;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static tv.esporx.framework.time.DateTimeUtils.*;

@Service
@Transactional
public class EventService {

    private static final int DEFAULT_LIVE_NOW_EVENT_COUNT = 10;
    private static final int DEFAULT_HOTTEST_EVENT_COUNT = 5;
    private static final int DEFAULT_UP_NEXT_EVENT_COUNT = 5;

    @Autowired
	private ChannelRepository channelRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private TimelineService timeline;

    @Transactional
	public void save(final long channelId, final long eventId) {
		Channel channel = channelRepository.findOne(channelId);
		Event event = eventRepository.findOne(eventId);
		channelRepository.save(channel);
		eventRepository.save(event);
	}
    
    /**
     * A live event is an event whose at least one currently ongoing (=in the current day slot) occurrence is on a live channel.
	 * Hottest are those sorted by the maximum viewer count of all their channels (remember an event can be broadcast to several channels).
     */
    public Collection<Event> findMostViewed() {
		return findMostViewed(DEFAULT_HOTTEST_EVENT_COUNT);
	}
	
    /**
     * @see EventService#findMostViewed()
     */
    public Collection<Event> findMostViewed(int nFirst) {
        Collection<Occurrence> filteredOccurrences = liveByViewerCountDesc();
        return transformToEvents(filteredOccurrences, nFirst);
	}

    /**
     * @return {limit} live occurrences per hour
     */
    public Map<DateTime, Collection<Occurrence>> findLiveNow() {
    	 return findLiveNow(DEFAULT_LIVE_NOW_EVENT_COUNT);
    }

    /**
     * @return {limit} live occurrences per hour
     */
    public Map<DateTime, Collection<Occurrence>> findLiveNow(int limit) {
        DateTime start = toStartHour(new DateTime());
        DateTime end = start.plusHours(2);
        Multimap<DateTime,Occurrence> occurrences = this.timeline.getTimeline(start, end).perHourMultimap();
        return retainFirstEntries(limit, filterValues(occurrences, new IsLiveOccurrenceFilter()).asMap());
    }

    public Collection<Occurrence> findUpNext() {
        return findUpNext(DEFAULT_UP_NEXT_EVENT_COUNT);
    }

    public Collection<Occurrence> findUpNext(int limit) {
        Collection<Occurrence> occurrences = newLinkedHashSet();
        DateTime start = toStartHour(new DateTime()).plusHours(1);
        DateTime end = new DateTime().plusHours(8);
        Iterator<Occurrence> iterator = timeline.getTimeline(start, end).perHourMultimap().values().iterator();
        for(int i = 0; i < limit && iterator.hasNext(); i++) {
            occurrences.add(iterator.next());
        }
        return occurrences;
    }

    private Map<DateTime, Collection<Occurrence>> retainFirstEntries(int limit, Map<DateTime, Collection<Occurrence>> map) {
        Map<DateTime, Collection<Occurrence>> result = new HashMap<DateTime, Collection<Occurrence>>();
        int added = 0;
        outer: for (Map.Entry<DateTime, Collection<Occurrence>> entries : map.entrySet()) {
            DateTime key = entries.getKey();
            Collection<Occurrence> occurrences = newArrayList();
            for (Occurrence occurrence : entries.getValue()) {
                if (added++ > limit) {
                    break outer;
                }
                occurrences.add(occurrence);
            }
            result.put(key, occurrences);
        }
        return result;
    }

    private Collection<Occurrence> liveByViewerCountDesc() {
        return filter(                                          //
            byViewerCountDesc(tillEndOfDay()),                  //
            new IsLiveOccurrenceFilter()                        //
        );
    }

    private Collection<Occurrence> tillEndOfDay() {
        DateTime start = toStartDay(new DateTime());
        DateTime end = toEndDay(start);
        return timeline.getTimeline(start, end).perHourMultimap().values();
    }

    private Collection<Occurrence> byViewerCountDesc(Collection<Occurrence> occurrences) {
        Collection<Occurrence> sortedOccurrences = new TreeSet<Occurrence>(new OccurrenceByMaxViewerComparator());
        sortedOccurrences.addAll(occurrences);
        return sortedOccurrences;
    }


    private Collection<Event> transformToEvents(Collection<Occurrence> filteredOccurrences, int limit) {
        Collection<Event> sortedLiveEvents = new LinkedHashSet<Event>();
        int loop = 0;
        for (Occurrence occurrence : filteredOccurrences) {
            if(loop++ > limit) {
                break;
            }
            sortedLiveEvents.add(occurrence.getEvent());
        }
        return sortedLiveEvents;
    }
}
