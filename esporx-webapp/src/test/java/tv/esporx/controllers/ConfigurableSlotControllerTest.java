package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.impl.ConfigurableSlotRepository;
import tv.esporx.domain.front.ConfigurableSlot;

public class ConfigurableSlotControllerTest {

	private ConfigurableSlot configurableSlot;
	private ConfigurableSlotController configurableSlotController;
	private ConfigurableSlotRepository slotDao;
	private HttpServletResponse servletResponse;

	@Before
	public void setup() {
		servletResponse = mock(HttpServletResponse.class);
		configurableSlotController = new ConfigurableSlotController();
		configurableSlot = new ConfigurableSlot();
		slotDao = Mockito.mock(ConfigurableSlotRepository.class);
		when(slotDao.findById(anyInt())).thenReturn(configurableSlot);
		configurableSlotController.setCastRepository(slotDao);
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
		verify(slotDao).saveOrUpdate(configurableSlot);
	}

}
