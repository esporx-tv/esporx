package tv.esporx.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.domain.Channel;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.OccurrenceRepository;
import tv.esporx.repositories.VideoProviderRepository;
import tv.esporx.services.BroadcastService;
import tv.esporx.services.TimelineService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ChannelControllerTest {

	private Channel channel;
	private ChannelController channelController;
	private ChannelRepository channelRepository;
	private VideoProviderRepository videoProviderRepository;
	private List<Channel> channels;

	@Before
	public void setup() {
        dummyChannel();
        mockedChannelRepository();
        mockedEventRepository();
		channelController = new ChannelController(channelRepository, videoProviderRepository, mock(TimelineService.class), mock(DomainClassConverter.class), mock(BroadcastService.class));
	}

    @Test
	public void when_accessing_channel_form_page_then_related_view_is_retrieved() {
		ModelAndView modelAndView = channelController.creation(new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("channel/form");
	}

	@Test
	public void when_accessing_channel_index_page_then_channel_is_retrieved() {
		channelController.index(10L, mock(HttpServletResponse.class));
		verify(channelRepository).findOne(anyLong());
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

    private void mockedEventRepository() {
        videoProviderRepository = mock(VideoProviderRepository.class);
        when(videoProviderRepository.getEmbeddedContents(anyString())).thenReturn("");
    }

    private void mockedChannelRepository() {
        channelRepository = mock(ChannelRepository.class);
        when(channelRepository.findOne(anyLong())).thenReturn(channel);
        when(channelRepository.findAll()).thenReturn(channels);
    }

    private void dummyChannel() {
        channel = new Channel();
        channel.setTitle("Channel Title");
        channel.setVideoUrl("http://site.com");
    }

}
