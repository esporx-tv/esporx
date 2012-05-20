package tv.esporx.dao.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableCast;
import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.dao.PersistenceCapableGame;
import tv.esporx.domain.Cast;
import tv.esporx.domain.Event;
import tv.esporx.domain.Game;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
"classpath:/META-INF/spring/testApplicationContext.xml"})
@Transactional
public class EventRepositoryIT {

	private Cast cast;

	@Autowired
	private PersistenceCapableCast castRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PersistenceCapableEvent eventRepository;

	private Game game;

	@Autowired
	private PersistenceCapableGame gameRepository;

	private Event insertedEvent;

	@Before
	public void setup() {
		givenDataHasBeenPurged();
		givenOneEventIsInserted();
	}

	@Test
	public void when_calling_delete_then_retrieved_event_is_null() {
		eventRepository.delete(insertedEvent);
		Event event = eventRepository.findById(insertedEvent.getId());
		assertThat(event).isNull();
	}

	@Test
	public void when_calling_findAll_then_only_the_inserted_event_is_returned() {
		List<Event> events = eventRepository.findAll();
		assertThat(events).containsOnly(insertedEvent);
	}

	@Test
	public void when_calling_findById_then_retrieved_event_is_the_same() {
		Event event = eventRepository.findById(insertedEvent.getId());
		assertThat(event).isEqualTo(insertedEvent);
		assertThat(event.getCasts()).containsOnly(cast);
	}

	@Test
	public void when_calling_findUpNext_then_the_inserted_event_is_returned() {
		DateTime rightNow = new DateTime();
		List<Event> events = eventRepository.findUpNext(rightNow);
		assertThat(events).isEmpty();
		DateTime beforeEventStartDate = new DateTime(new Date(100000L)).minusHours(1);
		events = eventRepository.findUpNext(beforeEventStartDate);
		assertThat(events).contains(insertedEvent);
	}

	@Test
	public void when_updating_event_then_retrieved_event_has_changes() {
		long previousId = insertedEvent.getId();
		String newTitle = "new title";
		insertedEvent.setTitle(newTitle);
		eventRepository.saveOrUpdate(insertedEvent);
		Event event = eventRepository.findById(insertedEvent.getId());
		assertThat(event.getId()).isEqualTo(previousId);
		assertThat(event.getTitle()).isEqualTo(newTitle);
		assertThat(event).isEqualTo(insertedEvent);
	}

	private void givenAtLeastOneGameIsStored() {
		insertDummyGame();
		game = gameRepository.findByTitle("Who gives a pluck?");
		assertThat(game).isNotNull();
	}

	private void givenDataHasBeenPurged() {
		entityManager.createNativeQuery("delete from games").executeUpdate();
		entityManager.createNativeQuery("delete from casts").executeUpdate();
		entityManager.createNativeQuery("delete from events").executeUpdate();
	}

	private void givenOneEventIsInserted() {
		givenOneCastIsInserted();
		insertedEvent = new Event();
		insertedEvent.setStartDate(new Date(100000L));
		insertedEvent.setTitle("TeH event of your life!");
		insertedEvent.setDescription("Just read this description and you'll be convinced");
		insertedEvent.setEndDate(new Date());
		insertedEvent.addCast(cast);
		insertedEvent.setHighlighted(false);
		assertThat(eventRepository).isNotNull();
		eventRepository.saveOrUpdate(insertedEvent);
		assertThat(insertedEvent.getId()).isGreaterThan(0L);
	}

	private void givenOneCastIsInserted() {
		givenAtLeastOneGameIsStored();
		cast = new Cast();
		cast.setRelatedGame(game);
		cast.setTitle("TeH cast");
		cast.setVideoUrl("http://not.what.you.think.of");
		cast.setBroadcastDate(new Date());
		cast.setViewerCount(2000000);
		cast.setDescription("defheuirhgeui");
		cast.setLanguage("fr");
		assertThat(castRepository).isNotNull();
		castRepository.saveOrUpdate(cast);
		assertThat(cast.getId()).isGreaterThan(0L);
	}

	private void insertDummyGame() {
		Game game = new Game();
		game.setTitle("Who gives a pluck?");
		game.setDescription("Birds are REALLY angry this time");
		gameRepository.saveOrUpdate(game);
	}

}
