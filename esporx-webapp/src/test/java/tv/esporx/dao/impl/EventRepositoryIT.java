package tv.esporx.dao.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableChannel;
import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.dao.PersistenceCapableGame;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Event;
import tv.esporx.domain.Game;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
"classpath:/META-INF/spring/testApplicationContext.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class EventRepositoryIT {

	private Channel channel;

	@Autowired
	private PersistenceCapableChannel channelRepository;

	@Autowired
	private PersistenceCapableEvent eventRepository;

	private Game game;

	@Autowired
	private PersistenceCapableGame gameRepository;

	private Event insertedEvent;

	@Before
	public void setup() {
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

	private void givenOneEventIsInserted() {
		givenOneChannelIsInserted();
		insertedEvent = new Event();
		insertedEvent.setTitle("TeH event of your life!");
		insertedEvent.setDescription("Just read this description and you'll be convinced");
		insertedEvent.setHighlighted(false);
		assertThat(eventRepository).isNotNull();
		eventRepository.saveOrUpdate(insertedEvent);
		assertThat(insertedEvent.getId()).isGreaterThan(0L);
	}

	private void givenOneChannelIsInserted() {
		givenAtLeastOneGameIsStored();
		channel = new Channel();
		channel.setTitle("TeH channel");
		channel.setVideoUrl("http://not.what.you.think.of");
		channel.setViewerCount(2000000);
		channel.setDescription("defheuirhgeui");
		channel.setLanguage("fr");
		assertThat(channelRepository).isNotNull();
		channelRepository.saveOrUpdate(channel);
		assertThat(channel.getId()).isGreaterThan(0L);
	}

	private void insertDummyGame() {
		Game game = new Game();
		game.setTitle("Who gives a pluck?");
		game.setDescription("Birds are REALLY angry this time");
		gameRepository.saveOrUpdate(game);
	}

}
