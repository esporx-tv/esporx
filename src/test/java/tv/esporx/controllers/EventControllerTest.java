package tv.esporx.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.domain.Event;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.EventRepository;
import tv.esporx.repositories.OccurrenceRepository;
import tv.esporx.services.EventService;

import javax.servlet.http.HttpServletResponse;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class EventControllerTest {

	private Event event;
	private EventController eventController;
	private EventRepository repository;
	private HttpServletResponse response;

	@Before
	public void setup() {
		response = mock(HttpServletResponse.class);
        givenDummyEvent();
        givenMockedEventRepository();
		eventController = new EventController(
            mock(OccurrenceRepository.class),
            repository,
            mock(DomainClassConverter.class)
        );
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
		verify(repository).findOne(anyLong());
	}

	@Test
	public void when_accessing_index_page_then_related_event_included() {
		ModelAndView modelAndView = eventController.index(1L, response);
		assertTrue(modelAndView.getModelMap().containsKey("event"));
	}

    private void givenMockedEventRepository() {
        repository = mock(EventRepository.class);
        when(repository.findOne(anyLong())).thenReturn(event);
    }

    private void givenDummyEvent() {
        event = new Event();
        event.setTitle("EventTitle");
        event.setDescription("description");
    }
}
