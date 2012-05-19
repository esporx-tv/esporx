package tv.esporx.dao.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableCast;
import tv.esporx.dao.PersistenceCapableGame;
import tv.esporx.dao.exceptions.PersistenceViolationException;
import tv.esporx.domain.Cast;
import tv.esporx.domain.Game;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
"classpath:/META-INF/spring/testApplicationContext.xml"})
@Transactional
public class CastRepositoryIT {

	@Autowired
	private PersistenceCapableCast castRepository;
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private PersistenceCapableGame gameRepository;
	private Cast leastViewedCast;
	private Cast mostWatchedCast;
	private Game relatedGame;
	private Cast retrievedCast;

	@Before
	public void setup() {
		givenDataHasBeenPurged();
		givenAtLeastOneGameIsStored();
		givenOneCastIsInserted();
		givenOneLesserWatchedCastIsInserted();
	}

	@Test
	public void when_a_new_cast_is_stored_then_it_is_retrievable_by_id() {
		retrievedCast = castRepository.findById(mostWatchedCast.getId());
		assertThat(retrievedCast).isEqualTo(mostWatchedCast);
	}

	@Test
	public void when_a_new_cast_is_stored_then_it_is_retrievable_by_title() {
		retrievedCast = castRepository.findByTitle("teh cast");
		assertThat(retrievedCast).isEqualTo(mostWatchedCast);
	}

	@Test
	public void when_calling_delete_then_retrieved_cast_is_null() {
		castRepository.delete(mostWatchedCast);
		retrievedCast = castRepository.findById(mostWatchedCast.getId());
		assertThat(retrievedCast).isNull();
	}

	@Test(expected = PersistenceViolationException.class)
	public void when_inserting_a_new_cast_with_already_stored_title_then_exception() {
		Cast cast = new Cast();
		cast.setRelatedGame(relatedGame);
		cast.setTitle("Pro evolution sucker");
		cast.setDescription("We need a description");
		cast.setVideoUrl("http://foo.bar");
		cast.setLanguage("fr");
		cast.setBroadcastDate(new Date());
		castRepository.saveOrUpdate(cast);
	}

	@Test(expected = PersistenceViolationException.class)
	public void when_inserting_a_new_cast_with_already_stored_video_url_then_exception() {
		Cast cast = new Cast();
		cast.setRelatedGame(relatedGame);
		cast.setTitle("Pro evolution sucker 2");
		cast.setVideoUrl("http://not.REALLY.what.you.think.of");
		cast.setBroadcastDate(new Date());
		cast.setDescription("en slip");
		cast.setLanguage("fr");
		assertThat(castRepository).isNotNull();
		castRepository.saveOrUpdate(cast);
		assertThat(cast.getId()).isGreaterThan(0L);
	}

	@Test
	public void when_retrieving_most_viewed_casts_then_most_viewed_are_returned_first() {
		List<Cast> mostViewedCasts = castRepository.findMostViewed();
		assertThat(mostViewedCasts.size()).isGreaterThanOrEqualTo(2);
		assertThatMostWatchedIsPositionedBeforeLeastWatched(mostViewedCasts);
	}

	@Test
	public void when_updating_a_cast_then_retrieved_cast_has_changes() {
		long previousId = mostWatchedCast.getId();
		mostWatchedCast.setTitle("Completely new title");
		castRepository.saveOrUpdate(mostWatchedCast);
		retrievedCast = castRepository.findById(previousId);
		assertThat(retrievedCast.getTitle()).isEqualTo("Completely new title");
		assertThat(castRepository.findByTitle("teh cast")).isNull();

	}

	private void assertThatMostWatchedIsPositionedBeforeLeastWatched(final List<Cast> mostViewedCasts) {
		int i = 0;
		int leastWatchedCastIndex = -1;
		int mostWatchedCastIndex = -1;
		for (Cast cast : mostViewedCasts) {
			if (cast.equals(leastViewedCast)) {
				leastWatchedCastIndex = i;
			}
			else if (cast.equals(mostWatchedCast)) {
				mostWatchedCastIndex = i;
			}
			i++;
		}
		assertThat(leastWatchedCastIndex).isNotEqualTo(-1);
		assertThat(mostWatchedCastIndex).isNotEqualTo(-1);
		assertThat(mostWatchedCastIndex).isLessThan(leastWatchedCastIndex);
	}

	private void givenAtLeastOneGameIsStored() {
		insertDummyGame();
		relatedGame = gameRepository.findByTitle("Who gives a pluck?");
		assertThat(relatedGame).isNotNull();
	}

	private void givenDataHasBeenPurged() {
		entityManager.createNativeQuery("delete from casts").executeUpdate();
		entityManager.createNativeQuery("delete from games").executeUpdate();
	}

	private void givenOneCastIsInserted() {
		mostWatchedCast = new Cast();
		mostWatchedCast.setRelatedGame(relatedGame);
		mostWatchedCast.setTitle("TeH cast");
		mostWatchedCast.setVideoUrl("http://not.what.you.think.of");
		mostWatchedCast.setBroadcastDate(new Date());
		mostWatchedCast.setViewerCount(2000000);
		mostWatchedCast.setDescription("en slip");
		mostWatchedCast.setLanguage("fr");
		assertThat(castRepository).isNotNull();
		castRepository.saveOrUpdate(mostWatchedCast);
		assertThat(mostWatchedCast.getId()).isGreaterThan(0L);
	}

	private void givenOneLesserWatchedCastIsInserted() {
		leastViewedCast = new Cast();
		leastViewedCast.setRelatedGame(relatedGame);
		leastViewedCast.setTitle("Pro evolution sucker");
		leastViewedCast.setVideoUrl("http://not.REALLY.what.you.think.of");
		leastViewedCast.setBroadcastDate(new Date());
		leastViewedCast.setViewerCount(5);
		leastViewedCast.setDescription("en slip");
		leastViewedCast.setLanguage("fr");
		assertThat(castRepository).isNotNull();
		castRepository.saveOrUpdate(leastViewedCast);
		assertThat(leastViewedCast.getId()).isGreaterThan(0L);
	}

	private void insertDummyGame() {
		Game game = new Game();
		game.setTitle("Who gives a pluck?");
		game.setDescription("Birds are REALLY angry this time");
		gameRepository.saveOrUpdate(game);
	}
}
