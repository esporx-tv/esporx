package tv.esporx.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.domain.ConfigurableSlot;
import tv.esporx.repositories.ConfigurableSlotRepository;

import javax.servlet.http.HttpServletResponse;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class ConfigurableSlotControllerTest {

	private ConfigurableSlot configurableSlot;
	private ConfigurableSlotController configurableSlotController;
	private ConfigurableSlotRepository repository;
	private HttpServletResponse servletResponse;

	@Before
	public void setup() {
		servletResponse = mock(HttpServletResponse.class);
		configurableSlot = new ConfigurableSlot();
		repository = mock(ConfigurableSlotRepository.class);
		when(repository.findOne(anyLong())).thenReturn(configurableSlot);
		configurableSlotController = new ConfigurableSlotController(repository, mock(DomainClassConverter.class));
	}


	@Test
	public void when_accessing_to_edition_page_then_view_is_retrieved() {
		ModelAndView modelAndView = configurableSlotController.edition(configurableSlot, servletResponse, new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("configurableSlot/form");
	}

	@Test
	public void when_accessing_to_the_form_page_then_is_retrieved() {
		ModelAndView modelAndView = configurableSlotController.creation(new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("configurableSlot/form");
	}

	@Test
	public void when_saving_a_configurable_slot_then_save_or_update_is_called() {
		BindingResult result = mock(BindingResult.class);
		configurableSlotController.save(configurableSlot, result, servletResponse, new ModelAndView());
		verify(repository).save(configurableSlot);
	}

}
