package tv.esporx.services;

import static com.google.common.collect.Sets.filter;
import static com.google.common.collect.Sets.newTreeSet;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.collections.comparators.OccurrenceByMaxViewerComparator;
import tv.esporx.collections.predicates.LiveOccurrencesFilter;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Event;
import tv.esporx.domain.Occurrence;
import tv.esporx.framework.time.DateTimeUtils;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.EventRepository;
import tv.esporx.services.timeline.Timeline;

@Service
@Transactional
public class EventService {

	private static final int DEFAULT_HOTTEST_EVENT_COUNT = 5;
	
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
        Set<Occurrence> filteredOccurrences = filter(           //
            sortByViewerCountDesc(occurrencesTillEndOfDay()),   //
            new LiveOccurrencesFilter()                         //
        );
        return transformToEvents(filteredOccurrences, nFirst);
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
}
