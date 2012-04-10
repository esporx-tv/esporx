package tv.esporx.dao.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

import tv.esporx.dao.PersistenceCapableCast;
import tv.esporx.domain.Cast;
import tv.esporx.domain.Game;

public class CastRepositoryTest {

	private Cast cast;
	private PersistenceCapableCast castDao;
	private EntityManager entityManager;
	private TypedQuery<Cast> query;
	private Game relatedGame;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		entityManager = mock(EntityManager.class);
		query = mock(TypedQuery.class);
		castDao = new CastRepository();
		when(query.getResultList()).thenReturn(new ArrayList<Cast>());
		relatedGame = new Game();
		when(entityManager.getReference(Game.class, 0L)).thenReturn(relatedGame);
		when(entityManager.createNamedQuery("Cast.findMostViewed", Cast.class)).thenReturn(query);
		castDao.setEntityManager(entityManager);
		cast = new Cast();
		cast.setRelatedGame(relatedGame);
	}

	@Test
	public void when_calling_delete_then_remove_on_entityManager_is_invoked() {
		castDao.delete(cast);
		verify(entityManager).remove(cast);
	}

	@Test
	public void when_calling_find_by_id_then_find_on_entityManager_is_invoked() {
		long id = 0L;
		castDao.findById(id);
		verify(entityManager).find(Cast.class, id);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_calling_find_by_id_with_negative_integer_then_throws_exception() {
		castDao.findById(-42);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_calling_find_by_title_with_null_title_then_throws_exception() {
		castDao.findByTitle(null);
	}

	@Test
	public void when_calling_find_most_viewed_then_getResultList_on_query_is_invoked() {
		castDao.findMostViewed();
		verify(query).getResultList();
	}

	@Test
	public void when_calling_save_or_update_then_persist_on_entityManager_is_invoked() {
		castDao.saveOrUpdate(cast);
		verify(entityManager).persist(cast);
	}

}
