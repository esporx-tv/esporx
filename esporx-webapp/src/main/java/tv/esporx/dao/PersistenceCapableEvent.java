package tv.esporx.dao;

import java.util.List;

import javax.persistence.EntityManager;

import tv.esporx.domain.Event;

public interface PersistenceCapableEvent {

	void delete(Event event);

	List<Event> findAll();

	Event findById(Long id);

	List<Event> findUpNext();

	void saveOrUpdate(Event event);

	void setEntityManager(EntityManager entityManager);

}
