package tv.esporx.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableChannel;
import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Event;

@Service
@Transactional
public class EventService {

	@Autowired
	private PersistenceCapableChannel channelDao;
	@Autowired
	private PersistenceCapableEvent eventDao;

	@Transactional
	public void saveOrUpdate(final long channelId, final long eventId) {
		Channel channel = channelDao.findById(channelId);
		Event event = eventDao.findById(eventId);
		channelDao.saveOrUpdate(channel);
		eventDao.saveOrUpdate(event);
	}
}
