package tv.esporx.dao.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

import tv.esporx.dao.PersistenceCapableChannel;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Game;

public class ChannelRepositoryTest {

	private Channel channel;
	private PersistenceCapableChannel channelDao;
	private EntityManager entityManager;
	private TypedQuery<Channel> query;
	private Game relatedGame;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		entityManager = mock(EntityManager.class);
		query = mock(TypedQuery.class);
		channelDao = new ChannelRepository();
		when(query.getResultList()).thenReturn(new ArrayList<Channel>());
		relatedGame = new Game();
		when(entityManager.getReference(Game.class, 0L)).thenReturn(relatedGame);
		when(entityManager.createNamedQuery("Channel.findMostViewed", Channel.class)).thenReturn(query);
		channelDao.setEntityManager(entityManager);
		channel = new Channel();
	}

	@Test
	public void when_calling_delete_then_remove_on_entityManager_is_invoked() {
		channelDao.delete(channel);
		verify(entityManager).remove(channel);
	}

	@Test
	public void when_calling_find_by_id_then_find_on_entityManager_is_invoked() {
		long id = 0L;
		channelDao.findById(id);
		verify(entityManager).find(Channel.class, id);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_calling_find_by_id_with_negative_integer_then_throws_exception() {
		channelDao.findById(-42);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_calling_find_by_title_with_null_title_then_throws_exception() {
		channelDao.findByTitle(null);
	}

	@Test
	public void when_calling_find_most_viewed_then_getResultList_on_query_is_invoked() {
		channelDao.findMostViewed();
		verify(query).getResultList();
	}

	@Test
	public void when_calling_save_or_update_then_persist_on_entityManager_is_invoked() {
		channelDao.saveOrUpdate(channel);
		verify(entityManager).persist(channel);
	}

}
