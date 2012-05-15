package tv.esporx.dao.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

import tv.esporx.dao.PersistenceCapableConfigurableSlot;
import tv.esporx.domain.front.ConfigurableSlot;

public class ConfigurableSlotRepositoryTest {

	private EntityManager entityManager;
	private TypedQuery<ConfigurableSlot> query;
	private ConfigurableSlot slot;
	private PersistenceCapableConfigurableSlot slotDao;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		entityManager = mock(EntityManager.class);
		query = mock(TypedQuery.class);
		slot = new ConfigurableSlot();
		when(query.getResultList()).thenReturn(new ArrayList<ConfigurableSlot>());
		when(entityManager.createNamedQuery("ConfigurableSlot.findAll", ConfigurableSlot.class)).thenReturn(query);
		when(entityManager.createNamedQuery("ConfigurableSlot.findByLanguage", ConfigurableSlot.class)).thenReturn(query);
		slotDao = new ConfigurableSlotRepository();
		slotDao.setEntityManager(entityManager);
	}

	@Test
	public void when_calling_find_then_getResultList_on_query_is_invoked() {
		slotDao.findByLanguage("en");
		verify(query).getResultList();
	}

	@Test
	public void when_calling_findById_then_find_on_entityManager_is_invoked() {
		long id = 0L;
		slotDao.findById(id);
		verify(entityManager).find(ConfigurableSlot.class, id);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_calling_findById_with_a_negative_integer_then_throws_exception() {
		slotDao.findById(-42);
	}

	@Test
	public void when_calling_saveOrUpdate_then_persist_on_entityManager_is_invoked() {
		slotDao.saveOrUpdate(slot);
		verify(entityManager).persist(slot);
	}
}
