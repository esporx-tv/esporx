package tv.esporx.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableCast;
import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.domain.Cast;
import tv.esporx.domain.Event;

@Service
@Transactional
public class EventService {

	@Autowired
	private PersistenceCapableCast castDao;
	@Autowired
	private PersistenceCapableEvent eventDao;

	@Transactional
	public void saveOrUpdate(final long castId, final long eventId) {
		Cast cast = castDao.findById(castId);
		Event event = eventDao.findById(eventId);
		cast.setEvent(event);
		castDao.saveOrUpdate(cast);
		event.addCast(cast);
		eventDao.saveOrUpdate(event);
	}
}
