package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.dao.PersistenceCapableVideoProvider;
import tv.esporx.dao.impl.ChannelRepository;
import tv.esporx.domain.Channel;

public class ChannelControllerTest {

	private Channel channel;
	private ChannelController channelController;
	private ChannelRepository channelDao;
	private PersistenceCapableVideoProvider videoProvider;
	private List<Channel> channels;

	@Before
	public void setup() {
		channelController = new ChannelController();
		setField(channelController, "eventDao", mock(PersistenceCapableEvent.class));
		channelDao = mock(ChannelRepository.class);
		channel = new Channel();
		channel.setTitle("Channel Title");
		channel.setVideoUrl("http://site.com");
		when(channelDao.findById(anyLong())).thenReturn(channel);
		setField(channelController, "channelDao", channelDao);
		videoProvider = mock(PersistenceCapableVideoProvider.class);
		when(videoProvider.getEmbeddedContents(anyString())).thenReturn("");
		channelController.setVideoProvider(videoProvider);
		when(channelDao.findAll()).thenReturn(channels);
	}


	@Test
	public void when_accessing_channel_form_page_then_related_view_is_retrieved() {
		ModelAndView modelAndView = channelController.creation(new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("channel/form");
	}

	@Test
	public void when_accessing_channel_index_page_then_channel_is_retrieved() {
		channelController.index(10L, mock(HttpServletResponse.class));
		verify(channelDao).findById(anyLong());
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_accessing_channel_index_page_with_negative_id_then_throws_exception() {
		channelController.index(-24L, mock(HttpServletResponse.class));
	}

	@Test
	public void when_accessing_channel_page_then_related_view_is_retrieved() {
		ModelAndView modelAndView = channelController.index(10L, mock(HttpServletResponse.class));
		assertThat(modelAndView.getViewName()).isEqualTo("channel/index");
	}

	@Test
	public void when_accessing_edition_page_then_edition_view_is_returned() {
		ModelAndView modelAndView = channelController.edition(channel, mock(HttpServletResponse.class), new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("channel/form");
	}

	@Test
	public void when_accessing_specific_channel_page_then_this_channel_is_retrieved() {
		ModelAndView modelAndView = channelController.index(10L, mock(HttpServletResponse.class));
		Map<String, Object> modelMap = modelAndView.getModel();
		assertThat(modelMap.get("channel")).isEqualTo(channel);
	}


}
