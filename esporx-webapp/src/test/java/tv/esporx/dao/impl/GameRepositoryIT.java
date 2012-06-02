package tv.esporx.dao.impl;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableGame;
import tv.esporx.domain.Game;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
"classpath:/META-INF/spring/testApplicationContext.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class GameRepositoryIT {

	private Game game;
	@Autowired
	private PersistenceCapableGame gameRepository;

	@Before
	public void setup() {
		givenOneInsertedGame();
	}

	@Test
	public void when_finding_game_by_id_then_retrieved_game_is_the_same() {
		Game retrievedGame = gameRepository.findById(game.getId());
		assertThat(retrievedGame).isEqualTo(game);
	}

	@Test
	public void when_finding_game_by_title_then_retrieved_game_is_the_same() {
		Game retrievedGame = gameRepository.findByTitle("world of wurstkraft");
		assertThat(retrievedGame).isEqualTo(game);
	}

	@Test
	public void when_updating_game_by_id_then_retrieved_game_has_changes() {
		long previousId = game.getId();
		String newTitle = "new title";
		game.setTitle(newTitle);
		gameRepository.saveOrUpdate(game);
		Game retrievedGame = gameRepository.findById(game.getId());
		assertThat(retrievedGame.getId()).isEqualTo(previousId);
		assertThat(retrievedGame.getTitle()).isEqualTo(newTitle);
		assertThat(retrievedGame).isEqualTo(game);
		assertThat(gameRepository.findByTitle("world of wurstkraft")).isNull();
	}

	private void givenOneInsertedGame() {
		game = new Game();
		game.setTitle("World of Wurstkraft");
		game.setDescription("Mit Curry, por favor");
		assertThat(gameRepository).isNotNull();
		gameRepository.saveOrUpdate(game);
		assertThat(game.getId()).isGreaterThan(0L);
	}
}
