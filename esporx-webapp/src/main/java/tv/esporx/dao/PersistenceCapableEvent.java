package tv.esporx.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;

import tv.esporx.domain.Event;

public interface PersistenceCapableEvent {

	void delete(Event event);

	List<Event> findAll();

	Event findById(long i);

	List<Event> findUpNext(DateTime from);

	void saveOrUpdate(Event event);

	void setEntityManager(EntityManager entityManager);

	List<Event> findTimeLine(DateTime from, DateTime to);

}
