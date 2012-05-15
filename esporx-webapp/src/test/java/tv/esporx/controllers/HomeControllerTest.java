package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.PersistenceCapableCast;
import tv.esporx.dao.impl.CastRepository;
import tv.esporx.dao.impl.ConfigurableSlotRepository;
import tv.esporx.dao.impl.EventRepository;
import tv.esporx.dao.impl.GameRepository;
import tv.esporx.dao.impl.GondolaSlideRepository;
import tv.esporx.framework.mvc.RequestUtils;

public class HomeControllerTest {

	private PersistenceCapableCast castDao;
	private EventRepository eventDao;
	private GameRepository gameDao;
	private GondolaSlideRepository gondolaDao;
	private HomeController homeController;
	private ConfigurableSlotRepository slotDao;
	private RequestUtils requestUtils;

	@Before
	public void setup() {
		homeController = new HomeController();
		castDao = mock(CastRepository.class);
		homeController.setCastRepository(castDao);
		eventDao = mock(EventRepository.class);
		homeController.setEventRepository(eventDao);
		gondolaDao = mock(GondolaSlideRepository.class);
		homeController.setGondolaRepository(gondolaDao);
		slotDao = mock(ConfigurableSlotRepository.class);
		homeController.setSlotRepository(slotDao);
		gameDao = mock(GameRepository.class);
		homeController.setGameRepository(gameDao);
		requestUtils = mock(RequestUtils.class);
		givenFrenchIsTheCurrentLanguage();
		homeController.setRequestHelper(requestUtils);
	}

	@Test
	public void when_accessing_index_page_then_configurable_slots_are_included() {
		ModelAndView modelAndView = homeController.index(null);
		assertTrue(modelAndView.getModelMap().containsKey("slots"));
	}

	@Test
	public void when_accessing_index_page_then_configurable_slots_are_retrieved() {
		homeController.index(mock(HttpServletRequest.class));
		verify(slotDao).findByLanguage("fr");
	}

	@Test
	public void when_accessing_index_page_then_gondola_slides_are_included() {
		ModelAndView modelAndView = homeController.index(mock(HttpServletRequest.class));
		assertTrue(modelAndView.getModelMap().containsKey("gondolaSlides"));
	}

	@Test
	public void when_accessing_index_page_then_most_viewed_casts_are_included() {
		ModelAndView modelAndView = homeController.index(mock(HttpServletRequest.class));
		assertTrue(modelAndView.getModelMap().containsKey("mostViewedCasts"));
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
		verify(gondolaDao).findByLanguage("fr");
	}

	@Test
	public void when_accessing_page_then_most_viewed_casts_are_retrieved() {
		homeController.index(null);
		verify(castDao).findMostViewed();
	}

	@Test
	public void when_accessing_page_then_most_viewed_events_are_retrieved() {
		homeController.index(null);
		verify(eventDao).findAll();
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
		verify(gameDao).findByTitle("toto");
	}

	@Test
	public void when_accessing_page_then_up_next_events_are_retrieved() {
		homeController.index(null);
		verify(eventDao).findUpNext(any(DateTime.class));
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
