package tv.esporx.dao.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.domain.Event;

public class EventRepositoryTest {

	private EntityManager entityManager;
	private Event event;
	private PersistenceCapableEvent eventDao;

	private TypedQuery<Event> query;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		query = mock(TypedQuery.class);
		when(query.getResultList()).thenReturn(new ArrayList<Event>());
		eventDao = new EventRepository();
		entityManager = mock(EntityManager.class);
		event = new Event();
		when(entityManager.merge(any(Event.class))).thenReturn(event);
		when(entityManager.createNamedQuery("Event.findUpNext", Event.class)).thenReturn(query);
		when(entityManager.createNamedQuery("Event.findAll", Event.class)).thenReturn(query);
		eventDao.setEntityManager(entityManager);
	}

	@Test
	public void when_calling_delete_then_remove_on_entity_manager_invoked() {
		eventDao.delete(event);
		verify(entityManager).remove(event);
	}

	@Test
	public void when_calling_find_by_up_next_then_get_query_list_on_entity_manager_invoked() {
		eventDao.findUpNext(new DateTime());
		verify(query).getResultList();
	}

	@Test
	public void when_calling_findById_then_find_on_entity_manager_invoked() {
		long primaryKey = 42L;
		eventDao.findById(primaryKey);
		verify(entityManager).find(Event.class, primaryKey);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_calling_findById_with_negative_integer_then_throws_exception() {
		eventDao.findById(-20);
	}

	@Test
	public void when_calling_findMostViewed_then_get_query_list_on_entity_manager_invoked() {
		eventDao.findAll();
		verify(query).getResultList();
	}

	@Test
	public void when_calling_save_or_update_then_persist_on_entity_manager_invoked() {
		eventDao.saveOrUpdate(event);
		verify(entityManager).persist(event);
	}
}
