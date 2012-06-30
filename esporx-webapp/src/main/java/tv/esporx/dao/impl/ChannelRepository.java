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

import tv.esporx.dao.PersistenceCapableChannel;
import tv.esporx.dao.exceptions.PersistenceViolationException;
import tv.esporx.domain.Channel;

@Repository
public class ChannelRepository implements PersistenceCapableChannel {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public void delete(final Channel channel) {
		entityManager.remove(channel);
	}

	@Override
	@Transactional(readOnly = true)
	public Channel findById(final long id) {
		checkArgument(id >= 0);
		return entityManager.find(Channel.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public Channel findByTitle(final String title) {
		checkArgument(title != null);
		TypedQuery<Channel> query = entityManager.createNamedQuery("Channel.findByTitle", Channel.class);
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
	public List<Channel> findMostViewed() {
		TypedQuery<Channel> query = entityManager.createNamedQuery("Channel.findMostViewed", Channel.class);
		return query.getResultList();
	}

	@Override
	@Transactional
	public void saveOrUpdate(final Channel channel) {
		try {
			entityManager.persist(channel);
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
	public List<Channel> findAll() {
		TypedQuery<Channel> query = entityManager.createNamedQuery("Channel.findAll", Channel.class);
		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Channel> findTimeLine(final DateTime from, final DateTime to) {
		TypedQuery<Channel> query = entityManager.createNamedQuery("Channel.findTimeLine", Channel.class);
		query.setParameter("date", from);
		query.setParameter("otherDate", to);
		return query.getResultList();
	}

}
