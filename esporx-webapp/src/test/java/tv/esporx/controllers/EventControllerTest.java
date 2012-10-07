package tv.esporx.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.dao.impl.EventRepository;
import tv.esporx.domain.Event;

import javax.servlet.http.HttpServletResponse;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

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
		event.setDescription("description");
		when(eventDao.findById(anyLong())).thenReturn(event);
		eventController.setEventRepository(eventDao);
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
		verify(eventDao).findById(anyLong());
	}

	@Test
	public void when_accessing_index_page_then_related_event_included() {
		ModelAndView modelAndView = eventController.index(1L, response);
		assertTrue(modelAndView.getModelMap().containsKey("event"));
	}

}
