package tv.esporx.dao.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.domain.Event;

@Repository
public class EventRepository implements PersistenceCapableEvent {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public void delete(final Event event) {
		Event mergedEvent = entityManager.merge(event);
		entityManager.remove(mergedEvent);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Event> findAll() {
		TypedQuery<Event> query = entityManager.createNamedQuery("Event.findAll", Event.class);
		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public Event findById(final long id) {
		checkArgument(id > 0);
		Event event = entityManager.find(Event.class, id);
		return event;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Event> findUpNext(final DateTime from) {
		TypedQuery<Event> query = entityManager.createNamedQuery("Event.findUpNext", Event.class);
		query.setParameter("date", from);
		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Event> findTimeLine(final DateTime from, final DateTime to) {
		TypedQuery<Event> query = entityManager.createNamedQuery("Event.findTimeLine", Event.class);
		query.setParameter("date", from);
		query.setParameter("otherDate", to);
		return query.getResultList();
	}

	@Override
	@Transactional
	public void saveOrUpdate(final Event event) {
		entityManager.persist(event);
	}

	@Override
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
