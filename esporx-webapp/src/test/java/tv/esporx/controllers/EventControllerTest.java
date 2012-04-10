package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.impl.EventRepository;
import tv.esporx.domain.Event;

public class EventControllerTest {

	private Event event;
	private EventController eventController;
	private EventRepository eventDao;
	private HttpServletResponse response;

	@Before
	public void setup() {
		response = mock(HttpServletResponse.class);
		eventController = new EventController();
		eventDao = mock(EventRepository.class);
		event = new Event();
		event.setTitle("EventTitle");
		when(eventDao.findById(anyInt())).thenReturn(event);
		eventController.setEventRepository(eventDao);
	}

	@Test
	public void when_accessing_edition_page_then_is_retrieved() {
		ModelAndView modelAndView = eventController.edition(1L, response);
		Map<String, Object> modelMap = modelAndView.getModel();
		assertThat(modelMap.get("eventCommand")).isEqualTo(event);

	}

	@Test
	public void when_accessing_form_page_then_is_retrieved() {
		ModelAndView modelAndView = eventController.creation();
		assertThat(modelAndView.getViewName()).isEqualTo("event/form");
	}

	@Test
	public void when_accessing_index_page_then_index_view_returned() {
		ModelAndView modelAndView = eventController.index(1L, response);
		assertThat(modelAndView.getViewName()).isEqualTo("event/index");
	}

	@Test
	public void when_accessing_index_page_then_is_retrieved() {
		eventController.index(1L, response);
		verify(eventDao).findById(anyInt());
	}

	@Test
	public void when_accessing_index_page_then_related_event_included() {
		ModelAndView modelAndView = eventController.index(1L, response);
		assertTrue(modelAndView.getModelMap().containsKey("event"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_deleting_cast_with_null_event_then_throws_exception() {
		eventController.delete(0);
	}

	@Test
	public void when_deleting_then_deletion_called() {
		eventController.delete(5L);
		verify(eventDao).delete(event);
	}
}
