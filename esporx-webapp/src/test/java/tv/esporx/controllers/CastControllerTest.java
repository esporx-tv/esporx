package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.PersistenceCapableVideoProvider;
import tv.esporx.dao.impl.CastRepository;
import tv.esporx.domain.Cast;

public class CastControllerTest {

	private Cast cast;
	private CastController castController;
	private CastRepository castDao;
	private HttpServletResponse response;
	private PersistenceCapableVideoProvider videoProvider;
	private List<Cast> casts;

	@Before
	public void setup() {
		response = mock(HttpServletResponse.class);
		castController = new CastController();
		castDao = mock(CastRepository.class);
		cast = new Cast();
		cast.setTitle("Cast Title");
		cast.setVideoUrl("http://site.com");
		when(castDao.findById(anyLong())).thenReturn(cast);
		castController.setCastRepository(castDao);
		videoProvider = mock(PersistenceCapableVideoProvider.class);
		when(videoProvider.getEmbeddedContents(anyString())).thenReturn("");
		castController.setVideoProvider(videoProvider);
		when(castDao.findAll()).thenReturn(casts);
	}


	@Test
	public void when_accessing_cast_form_page_then_related_view_is_retrieved() {
		ModelAndView modelAndView = castController.creation(new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("cast/form");
	}

	@Test
	public void when_accessing_cast_index_page_then_cast_is_retrieved() {
		castController.index(10L, mock(HttpServletResponse.class));
		verify(castDao).findById(anyLong());
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_accessing_cast_index_page_with_negative_id_then_throws_exception() {
		castController.index(-24L, mock(HttpServletResponse.class));
	}

	@Test
	public void when_accessing_cast_page_then_related_view_is_retrieved() {
		ModelAndView modelAndView = castController.index(10L, mock(HttpServletResponse.class));
		assertThat(modelAndView.getViewName()).isEqualTo("cast/index");
	}

	@Test
	public void when_accessing_edition_page_then_edition_view_is_returned() {
		ModelAndView modelAndView = castController.edition(cast, mock(HttpServletResponse.class), new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("cast/form");
	}

	@Test
	public void when_accessing_specific_cast_page_then_this_cast_is_retrieved() {
		ModelAndView modelAndView = castController.index(10L, mock(HttpServletResponse.class));
		Map<String, Object> modelMap = modelAndView.getModel();
		assertThat(modelMap.get("cast")).isEqualTo(cast);
	}

	@Test
	public void when_deleting_cast_then_cast_is_deleted() {
		castController.delete(9L, response);
		verify(castDao).delete(cast);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_deleting_cast_with_negative_long_then_throws_exception() {
		castController.delete(-2L, response);
	}
	
}
