package tv.esporx.dao.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableCast;
import tv.esporx.dao.exceptions.PersistenceViolationException;
import tv.esporx.domain.Cast;
import tv.esporx.domain.Event;

@Repository
public class CastRepository implements PersistenceCapableCast {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public void delete(final Cast cast) {
		entityManager.remove(cast);
	}

	@Override
	@Transactional(readOnly = true)
	public Cast findById(final long id) {
		checkArgument(id >= 0);
		return entityManager.find(Cast.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public Cast findByTitle(final String title) {
		checkArgument(title != null);
		TypedQuery<Cast> query = entityManager.createNamedQuery("Cast.findByTitle", Cast.class);
		query.setParameter("title", title.toUpperCase()).setMaxResults(1);
		try {
			return query.getSingleResult();
		}
		catch (NoResultException nre) {
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cast> findMostViewed() {
		TypedQuery<Cast> query = entityManager.createNamedQuery("Cast.findMostViewed", Cast.class);
		return query.getResultList();
	}

	@Override
	@Transactional
	public void saveOrUpdate(final Cast cast) {
		try {
			Event relatedEvent = cast.getEvent();
			if (relatedEvent != null) {
				// TODO: move me elsewhere, cast dao shouldn't know about events
				cast.setEvent(entityManager.find(Event.class, relatedEvent.getId()));
			}
			entityManager.persist(cast);
		}
		catch (PersistenceException e) {
			throw new PersistenceViolationException(e);
		}
	}

	@Override
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cast> findAll() {
		TypedQuery<Cast> query = entityManager.createNamedQuery("Cast.findAll", Cast.class);
		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cast> findTimeLine(final DateTime from, final DateTime to) {
		TypedQuery<Cast> query = entityManager.createNamedQuery("Cast.findTimeLine", Cast.class);
		query.setParameter("date", from);
		query.setParameter("otherDate", to);
		return query.getResultList();
	}

}
