package tv.esporx.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Event;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.EventRepository;

@Service
@Transactional
public class EventService {

	@Autowired
	private ChannelRepository channelRepository;
	@Autowired
	private EventRepository eventRepository;

    @Transactional
	public void save(final long channelId, final long eventId) {
		Channel channel = channelRepository.findOne(channelId);
		Event event = eventRepository.findOne(eventId);
		channelRepository.save(channel);
		eventRepository.save(event);
	}
}
