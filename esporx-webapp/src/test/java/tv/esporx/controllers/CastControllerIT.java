package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.impl.CastRepository;
import tv.esporx.dao.impl.GameRepository;
import tv.esporx.domain.Cast;
import tv.esporx.domain.Game;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
"classpath:/META-INF/spring/testApplicationContext.xml" })
public class CastControllerIT {
	private BindingResult bindingResult;
	private Cast cast;

	@Autowired
	private CastController castController;
	private CastRepository castDao;
	private GameRepository gameDao;

	@Autowired
	private Validator validator;
	private Game game;

	@Before
	public void setup() {
		givenOneGame();
		givenGameRepositoryIsMocked();
		givenOneCast();
		givenCastRepositoryIsMocked();
	}

	private void givenOneCast() {
		cast = new Cast();
		cast.setTitle("Cast Title");
		cast.setVideoUrl("http://site.com");
		cast.setBroadcastDate(new Date());
		cast.setDescription("Zuper description");
		cast.setLanguage("fr");
	}

	private void givenOneGame() {
		game = new Game();
		game.setTitle("starcraft2");
	}

	private void givenCastRepositoryIsMocked() {
		castDao = mock(CastRepository.class);
		when(castDao.findById(anyInt())).thenReturn(cast);
		assertThat(castController).isNotNull();
		castController.setCastRepository(castDao);
	}

	private void givenGameRepositoryIsMocked() {
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Query query = Mockito.mock(Query.class);
		when(query.setMaxResults(Mockito.anyInt())).thenReturn(query);
		when(entityManager.createNamedQuery("Game.findByTitle")).thenReturn(query);
		gameDao = mock(GameRepository.class);
		when(gameDao.findByTitle(Mockito.anyString())).thenReturn(game);
		castController.setGameRepository(gameDao);
	}


	@Test
	public void when_saving_cast_then_cast_is_persisted() {
		givenBeanHasBeenValidated();
		castController.save(cast, bindingResult, new MockHttpServletRequest(), new ModelAndView());
		verify(castDao).saveOrUpdate(cast);
	}

	@Test
	public void when_saving_is_successful_then_admin_homepageview_is_returned() {
		givenBeanHasBeenValidated();
		ModelAndView modelAndView = castController.save(cast, bindingResult, new MockHttpServletRequest(), new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("redirect:/admin/home");
	}

	@Test
	public void when_saving_is_unsuccessful_then_homepageview_is_returned() {
		givenBeanHasBeenInvalidated();
		ModelAndView modelAndView = castController.save(cast, bindingResult, new MockHttpServletRequest(), new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("cast/form");
	}

	private void givenBeanHasBeenInvalidated() {
		cast = new Cast();
		bindingResult = new BeanPropertyBindingResult(cast, "castCommand");
		validator.validate(cast, bindingResult);
		assertThat(bindingResult.getErrorCount()).isGreaterThan(0);
	}

	private void givenBeanHasBeenValidated() {
		bindingResult = new BeanPropertyBindingResult(cast, "castCommand");
		validator.validate(cast, bindingResult);
		assertThat(bindingResult.getErrorCount()).isEqualTo(0);
	}

}
