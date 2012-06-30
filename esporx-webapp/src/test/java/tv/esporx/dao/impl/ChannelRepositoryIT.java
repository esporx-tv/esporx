package tv.esporx.dao.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

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
import tv.esporx.dao.PersistenceCapableGame;
import tv.esporx.dao.exceptions.PersistenceViolationException;
import tv.esporx.domain.Channel;
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
public class ChannelRepositoryIT {

	@Autowired
	private PersistenceCapableChannel channelRepository;
	@Autowired
	private PersistenceCapableGame gameRepository;
	private Channel leastViewedChannel;
	private Channel mostWatchedChannel;
	private Game relatedGame;
	private Channel retrievedChannel;

	@Before
	public void setup() {
		givenAtLeastOneGameIsStored();
		givenOneChannelIsInserted();
		givenOneLesserWatchedChannelIsInserted();
	}

	@Test
	public void when_a_new_channel_is_stored_then_it_is_retrievable_by_id() {
		retrievedChannel = channelRepository.findById(mostWatchedChannel.getId());
		assertThat(retrievedChannel).isEqualTo(mostWatchedChannel);
	}

	@Test
	public void when_a_new_channel_is_stored_then_it_is_retrievable_by_title() {
		retrievedChannel = channelRepository.findByTitle("teh channel");
		assertThat(retrievedChannel).isEqualTo(mostWatchedChannel);
	}

	@Test
	public void when_calling_delete_then_retrieved_channel_is_null() {
		channelRepository.delete(mostWatchedChannel);
		retrievedChannel = channelRepository.findById(mostWatchedChannel.getId());
		assertThat(retrievedChannel).isNull();
	}

	@Test(expected = PersistenceViolationException.class)
	public void when_inserting_a_new_channel_with_already_stored_title_then_exception() {
		Channel channel = new Channel();
		channel.setTitle("Pro evolution sucker");
		channel.setDescription("We need a description");
		channel.setVideoUrl("http://foo.bar");
		channel.setLanguage("fr");
		channelRepository.saveOrUpdate(channel);
	}

	@Test(expected = PersistenceViolationException.class)
	public void when_inserting_a_new_channel_with_already_stored_video_url_then_exception() {
		Channel channel = new Channel();
		channel.setTitle("Pro evolution sucker 2");
		channel.setVideoUrl("http://not.REALLY.what.you.think.of");
		channel.setDescription("en slip");
		channel.setLanguage("fr");
		assertThat(channelRepository).isNotNull();
		channelRepository.saveOrUpdate(channel);
		assertThat(channel.getId()).isGreaterThan(0L);
	}

	@Test
	public void when_retrieving_most_viewed_channels_then_most_viewed_are_returned_first() {
		List<Channel> mostViewedChannels = channelRepository.findMostViewed();
		assertThat(mostViewedChannels.size()).isGreaterThanOrEqualTo(2);
		assertThatMostWatchedIsPositionedBeforeLeastWatched(mostViewedChannels);
	}

	@Test
	public void when_updating_a_channel_then_retrieved_channel_has_changes() {
		long previousId = mostWatchedChannel.getId();
		mostWatchedChannel.setTitle("Completely new title");
		channelRepository.saveOrUpdate(mostWatchedChannel);
		retrievedChannel = channelRepository.findById(previousId);
		assertThat(retrievedChannel.getTitle()).isEqualTo("Completely new title");
		assertThat(channelRepository.findByTitle("teh channel")).isNull();

	}

	private void assertThatMostWatchedIsPositionedBeforeLeastWatched(final List<Channel> mostViewedChannels) {
		int i = 0;
		int leastWatchedChannelIndex = -1;
		int mostWatchedChannelIndex = -1;
		for (Channel channel : mostViewedChannels) {
			if (channel.equals(leastViewedChannel)) {
				leastWatchedChannelIndex = i;
			}
			else if (channel.equals(mostWatchedChannel)) {
				mostWatchedChannelIndex = i;
			}
			i++;
		}
		assertThat(leastWatchedChannelIndex).isNotEqualTo(-1);
		assertThat(mostWatchedChannelIndex).isNotEqualTo(-1);
		assertThat(mostWatchedChannelIndex).isLessThan(leastWatchedChannelIndex);
	}

	private void givenAtLeastOneGameIsStored() {
		insertDummyGame();
		relatedGame = gameRepository.findByTitle("Who gives a pluck?");
		assertThat(relatedGame).isNotNull();
	}

	private void givenOneChannelIsInserted() {
		mostWatchedChannel = new Channel();
		mostWatchedChannel.setTitle("TeH channel");
		mostWatchedChannel.setVideoUrl("http://not.what.you.think.of");
		mostWatchedChannel.setViewerCount(2000000);
		mostWatchedChannel.setDescription("en slip");
		mostWatchedChannel.setLanguage("fr");
		assertThat(channelRepository).isNotNull();
		channelRepository.saveOrUpdate(mostWatchedChannel);
		assertThat(mostWatchedChannel.getId()).isGreaterThan(0L);
	}

	private void givenOneLesserWatchedChannelIsInserted() {
		leastViewedChannel = new Channel();
		leastViewedChannel.setTitle("Pro evolution sucker");
		leastViewedChannel.setVideoUrl("http://not.REALLY.what.you.think.of");
		leastViewedChannel.setViewerCount(5);
		leastViewedChannel.setDescription("en slip");
		leastViewedChannel.setLanguage("fr");
		assertThat(channelRepository).isNotNull();
		channelRepository.saveOrUpdate(leastViewedChannel);
		assertThat(leastViewedChannel.getId()).isGreaterThan(0L);
	}

	private void insertDummyGame() {
		Game game = new Game();
		game.setTitle("Who gives a pluck?");
		game.setDescription("Birds are REALLY angry this time");
		gameRepository.saveOrUpdate(game);
	}
}
