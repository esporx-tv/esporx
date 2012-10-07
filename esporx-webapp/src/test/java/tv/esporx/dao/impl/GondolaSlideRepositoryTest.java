package tv.esporx.dao.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

import tv.esporx.dao.PersistenceCapableGondolaSlide;
import tv.esporx.domain.front.GondolaSlide;

public class GondolaSlideRepositoryTest {

	private EntityManager entityManager;
	private PersistenceCapableGondolaSlide gondolaDao;
	private GondolaSlide gondolaSlide;
	private TypedQuery<GondolaSlide> query;

	@Before
	@SuppressWarnings("unchecked")
	public void setup() {
		entityManager = mock(EntityManager.class);
		query = mock(TypedQuery.class);
		when(query.getResultList()).thenReturn(new ArrayList<GondolaSlide>());
		gondolaSlide = new GondolaSlide();
		gondolaDao = new GondolaSlideRepository();
		gondolaDao.setEntityManager(entityManager);
	}

	@Test
	public void when_calling_findById_then_find_on_entityManager_is_invoked() {
		long id = 0L;
		gondolaDao.findById(id);
		verify(entityManager).find(GondolaSlide.class, id);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_calling_findById_with_a_negative_integer_then_throws_exception() {
		gondolaDao.findById(-42L);
	}

	@Test
	public void when_calling_findByLanguage_then_findByResult_on_query_is_invoked() {
		when(entityManager.createNamedQuery("GondolaSlide.findByLanguage", GondolaSlide.class)).thenReturn(query);
		gondolaDao.findByLanguage("fr");
		verify(query).getResultList();
	}

	@Test
	public void when_calling_saveOrUpdate_then_persist_on_entityManager_is_invoked() {
		gondolaDao.saveOrUpdate(gondolaSlide);
		verify(entityManager).persist(gondolaSlide);
	}

}
