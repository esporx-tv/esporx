package tv.esporx.services;

import static com.google.common.collect.Sets.filter;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static com.google.common.collect.Sets.newTreeSet;

import java.util.*;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.collections.comparators.OccurrenceByMaxViewerComparator;
import tv.esporx.collections.predicates.IsLiveOccurrenceFilter;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Event;
import tv.esporx.domain.Occurrence;
import tv.esporx.framework.time.DateTimeUtils;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.EventRepository;

@Service
@Transactional
public class EventService {

    private static final int DEFAULT_LIVE_NOW_EVENT_COUNT = 10;
    private static final int DEFAULT_HOTTEST_EVENT_COUNT = 5;
    public static final int DEFAULT_UP_NEXT_EVENT_COUNT = 5;

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
    public Set<Event> findMostViewed() {
		return findMostViewed(DEFAULT_HOTTEST_EVENT_COUNT);
	}
	
    /**
     * @see EventService#findMostViewed()
     */
    public Set<Event> findMostViewed(int nFirst) {
        Set<Occurrence> filteredOccurrences = filteredLiveOccurrenceByViewerCount();
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
    	 return timeline.getTimeline().occurrencesPerHoursAt(   //
                 new DateTime().minusHours(1),                  //
                 new DateTime().plusHours(2),                   //
                 limit                                          //
         );
    }

    private Set<Occurrence> filteredLiveOccurrenceByViewerCount() {
        return filter(                                              //
                sortByViewerCountDesc(occurrencesTillEndOfDay()),   //
                new IsLiveOccurrenceFilter()                        //
            );
    }

    private Set<Occurrence> occurrencesTillEndOfDay() {
        DateTime start = DateTimeUtils.toStartDay(new DateTime());
        DateTime end = DateTimeUtils.toEndDay(start);
        return timeline.getTimeline().occurrencesBetween(start, end);
    }

    private Set<Occurrence> sortByViewerCountDesc(Set<Occurrence> occurrences) {
        Set<Occurrence> sortedOccurrences = new TreeSet<Occurrence>(new OccurrenceByMaxViewerComparator());
        sortedOccurrences.addAll(occurrences);
        return sortedOccurrences;
    }

    private Set<Event> transformToEvents(Set<Occurrence> filteredOccurrences, int limit) {
        LinkedHashSet<Event> sortedLiveEvents = new LinkedHashSet<Event>();
        int loop = 0;
        for (Occurrence occurrence : filteredOccurrences) {
            if(loop++ > limit) {
                break;
            }
            sortedLiveEvents.add(occurrence.getEvent());
        }
        return sortedLiveEvents;
    }


    public Set<Occurrence> findUpNext() {
       return findUpNext(DEFAULT_UP_NEXT_EVENT_COUNT);
    }

    public Set<Occurrence> findUpNext(int limit) {
        Set<Occurrence> occurrences = newLinkedHashSet();
        Iterator<Occurrence> iterator = timeline.getTimeline().occurrencesBetween(new DateTime().plusHours(1), new DateTime().plusHours(8)).iterator();
        for(int i = 0; i < limit && iterator.hasNext(); i++) {
            occurrences.add(iterator.next());
        }
        return occurrences;
    }
}
