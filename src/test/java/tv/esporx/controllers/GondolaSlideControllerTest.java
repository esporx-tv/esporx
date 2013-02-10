package tv.esporx.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.domain.GondolaSlide;
import tv.esporx.repositories.GondolaSlideRepository;

import javax.servlet.http.HttpServletResponse;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class GondolaSlideControllerTest {

	private GondolaSlideController gondolaController;
	private GondolaSlideRepository gondolaDaoRepository;
	private GondolaSlide gondolaSlide;
	private HttpServletResponse response;

	@Before
	public void setup() {
		response = mock(HttpServletResponse.class);
		gondolaSlide = new GondolaSlide();
		gondolaDaoRepository = mock(GondolaSlideRepository.class);
		when(gondolaDaoRepository.findOne(anyLong())).thenReturn(gondolaSlide);
		gondolaController = new GondolaSlideController(gondolaDaoRepository, mock(DomainClassConverter.class));
	}

	@Test(expected = NullPointerException.class)
	public void when_accessing_edition_page_then_exception() {
		gondolaController.creation(null);
	}

	@Test
	public void when_accessing_edition_page_then_view_is_retrieved() {
		ModelAndView modelAndView = gondolaController.edition(gondolaSlide, response, new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("slide/form");

	}

	@Test
	public void when_saving_a_gondola_slide_then_saveOrUpdate_on_dao_is_invoked() {
		BindingResult result = mock(BindingResult.class);
		gondolaController.save(gondolaSlide, result, null);
		verify(gondolaDaoRepository).save(gondolaSlide);
	}

	@Test
	public void when_submitting_slide_to_delete_then_view_is_retrieved() {
		ModelAndView modelAndView = gondolaController.delete(0, response);
		assertThat(modelAndView.getViewName()).isEqualTo("redirect:/admin/home?active=gondola");
	}

	@Test
	public void when_submitting_invalid_slide_to_delete_then_throw_exception() {
		when(gondolaDaoRepository.findOne(Mockito.anyLong())).thenReturn(null);
		ModelAndView modelAndView = gondolaController.delete(-15, response);
		assertThat(modelAndView.getViewName()).isEqualTo("channel/notFound");
	}
}
