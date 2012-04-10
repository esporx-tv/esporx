package tv.esporx.dao.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.persistence.EntityManager;

import org.junit.Test;

import tv.esporx.domain.Cast;


public class CastRepositoryTest {
	
	@Test
	public void when_doing_saveOrUpdate_then_persist_is_called() {
		EntityManager entityManager = mock(EntityManager.class);
		CastRepository castDao = new CastRepository();
		castDao.setEntityManager(entityManager);
		Cast cast = new Cast();
		castDao.saveOrUpdate(cast);
		verify(entityManager, times(1)).persist(cast);
	}

}
