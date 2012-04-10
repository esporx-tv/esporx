package tv.esporx.dao.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;

import tv.esporx.dao.PersistenceCapableGame;
import tv.esporx.domain.Game;

public class GameRepositoryTest {

	private EntityManager entityManager;
	private Game game;
	private PersistenceCapableGame gameDao;
	private Query query;

	@Before
	public void setup() {
		givenOneGame();
		givenOneMockedQuery();
		givenOneMockedEntityManager();
		prepareGameRepository();
	}

	@Test
	public void when_calling_findByTitle_then_find_is_called() {
		gameDao.findByTitle("Starcraft 2");
		verify(entityManager).createNamedQuery("Game.findByTitle");
		verify(query).getSingleResult();
	}

	@Test
	public void when_calling_saveOrUpdate_then_persist_is_called() {
		gameDao.saveOrUpdate(game);
		verify(entityManager).persist(game);
	}

	private void givenOneGame() {
		game = new Game();
		game.setTitle("Starcraft 2");
		game.setDescription("This is a huge strategy game");
	}

	private void givenOneMockedEntityManager() {
		entityManager = mock(EntityManager.class);
		when(entityManager.createNamedQuery("Game.findByTitle")).thenReturn(query);
		doNothing().when(entityManager).persist(any(Game.class));
	}

	private void givenOneMockedQuery() {
		query = mock(Query.class);
		when(query.setParameter(anyString(), anyObject())).thenReturn(query);
		when(query.getSingleResult()).thenReturn(game);
	}

	private void prepareGameRepository() {
		gameDao = new GameRepository();
		gameDao.setEntityManager(entityManager);
	}

}
