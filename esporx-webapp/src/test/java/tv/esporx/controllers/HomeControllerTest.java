package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.framework.mvc.RequestUtils;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.ConfigurableSlotRepository;
import tv.esporx.repositories.EventRepository;
import tv.esporx.repositories.GameRepository;
import tv.esporx.repositories.GondolaSlideRepository;
import tv.esporx.services.EventService;

public class HomeControllerTest {

	private ChannelRepository channelRepository;
	private EventRepository eventRepository;
	private GameRepository gameRepository;
	private GondolaSlideRepository gondolaRepository;
	private HomeController homeController;
	private ConfigurableSlotRepository slotRepository;
	private RequestUtils requestUtils;
	private EventService eventService;

    @Before
	public void setup() {
		channelRepository = mock(ChannelRepository.class);
		eventRepository = mock(EventRepository.class);
		gondolaRepository = mock(GondolaSlideRepository.class);
		slotRepository = mock(ConfigurableSlotRepository.class);
		gameRepository = mock(GameRepository.class);
		eventService = mock(EventService.class);
		requestUtils = mock(RequestUtils.class);
        givenFrenchIsTheCurrentLanguage();
        homeController = new HomeController(channelRepository, eventRepository, gameRepository, gondolaRepository, slotRepository, requestUtils, eventService);
	}

	@Test
	public void when_accessing_index_page_then_configurable_slots_are_included() {
		ModelAndView modelAndView = homeController.index(null);
		assertTrue(modelAndView.getModelMap().containsKey("slots"));
	}

	@Test
	public void when_accessing_index_page_then_configurable_slots_are_retrieved() {
		homeController.index(mock(HttpServletRequest.class));
		verify(slotRepository).findByLanguage("fr");
	}

	@Test
	public void when_accessing_index_page_then_gondola_slides_are_included() {
		ModelAndView modelAndView = homeController.index(mock(HttpServletRequest.class));
		assertTrue(modelAndView.getModelMap().containsKey("gondolaSlides"));
	}

	@Test
	public void when_accessing_index_page_then_most_viewed_channels_are_included() {
		ModelAndView modelAndView = homeController.index(mock(HttpServletRequest.class));
		assertTrue(modelAndView.getModelMap().containsKey("mostViewedChannels"));
	}

	@Test
	public void when_accessing_index_page_then_most_viewed_events_are_included() {
		ModelAndView modelAndView = homeController.index(mock(HttpServletRequest.class));
		assertTrue(modelAndView.getModelMap().containsKey("mostViewedEvents"));
	}

	@Test
	public void when_accessing_index_page_then_up_next_events_are_included() {
		ModelAndView modelAndView = homeController.index(mock(HttpServletRequest.class));
		assertTrue(modelAndView.getModelMap().containsKey("upNextEvents"));
	}

	@Test
	public void when_accessing_page_then_gondola_slides_are_retrieved() {
		homeController.index(mock(HttpServletRequest.class));
		verify(gondolaRepository).findByLanguage("fr");
	}

	@Test
	public void when_accessing_page_then_most_viewed_channels_are_retrieved() {
		homeController.index(null);
		verify(channelRepository).findMostViewed();
	}

	@Test
	public void when_accessing_page_then_most_viewed_events_are_retrieved() {
		homeController.index(null);
		verify(eventService).findMostViewed();
	}

	@Test
	public void when_accessing_page_then_related_game_is_included() {
		ModelAndView modelAndView = homeController.index(null);
		assertTrue(modelAndView.getModelMap().containsKey("game"));
	}

	@Test
	public void when_accessing_page_then_related_game_is_retrieved() {
		givenTotoIsTheCurrentGame();
		homeController.index(null);
		verify(gameRepository).findByTitle("toto");
	}

	@Test
	public void when_accessing_page_then_up_next_events_are_retrieved() {
		homeController.index(null);
		verify(eventService).findUpNext();
	}

	@Test
	public void when_accessing_page_then_view_is_returned() {
		ModelAndView modelAndView = homeController.index(null);
		assertThat(modelAndView.getViewName()).isEqualTo("home");
	}

	private void givenTotoIsTheCurrentGame() {
		when(requestUtils.currentGame(any(HttpServletRequest.class))).thenReturn("toto");
	}

	private void givenFrenchIsTheCurrentLanguage() {
		when(requestUtils.currentLocale(any(HttpServletRequest.class))).thenReturn("fr");
	}
}
