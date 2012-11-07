package tv.esporx.services;

import static com.google.common.collect.Sets.filter;
import static com.google.common.collect.Sets.newTreeSet;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.collections.LiveOccurrencesFilterPredicate;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Event;
import tv.esporx.domain.Occurrence;
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
    
    public Set<Event> findMostViewed() {
		return findMostViewed(DEFAULT_HOTTEST_EVENT_COUNT);
	}
	
    public Set<Event> findMostViewed(int limit) {
        Timeline cache = timeline.getTimeline();
        Set<Occurrence> occurrences = cache.allOccurrences();
        Set<Occurrence> sortedOccurrences = new TreeSet<Occurrence>(new Comparator<Occurrence>() {
			
        	@Override
			public int compare(Occurrence occ1, Occurrence occ2) {
				Integer firstMaxViewerCount = getMaxViewerCount(occ1);
				Integer secondMaxViewerCount = getMaxViewerCount(occ2);
				return firstMaxViewerCount.compareTo(secondMaxViewerCount);
			}

			private Integer getMaxViewerCount(Occurrence occ1) {
				Set<Channel> sortedChannels = newTreeSet(Channel.COMPARATOR_BY_MAX_VIEWER_COUNT);
				sortedChannels.addAll(occ1.getChannels()); 
				return Integer.valueOf(sortedChannels.iterator().next().getViewerCount());
			}
        	
        });
        sortedOccurrences.addAll(occurrences);
        Set<Occurrence> filteredOccurrences = filter(sortedOccurrences, new LiveOccurrencesFilterPredicate());
        
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
