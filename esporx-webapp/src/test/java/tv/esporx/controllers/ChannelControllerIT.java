package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.impl.ChannelRepository;
import tv.esporx.dao.impl.GameRepository;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Game;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
"classpath:/META-INF/spring/testApplicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class ChannelControllerIT {
	private BindingResult bindingResult;
	private Channel channel;

	@Autowired
	private ChannelController channelController;
	private ChannelRepository channelDao;
	private GameRepository gameDao;

	@Autowired
	private Validator validator;
	private Game game;

	@Before
	public void setup() {
		givenOneGame();
		givenGameRepositoryIsMocked();
		givenOneChannel();
		givenChannelRepositoryIsMocked();
	}

	private void givenOneChannel() {
		channel = new Channel();
		channel.setTitle("Channel Title");
		channel.setVideoUrl("http://site.com");
		channel.setDescription("Zuper description");
		channel.setLanguage("fr");
	}

	private void givenOneGame() {
		game = new Game();
		game.setTitle("starcraft2");
	}

	private void givenChannelRepositoryIsMocked() {
		channelDao = mock(ChannelRepository.class);
		when(channelDao.findById(anyLong())).thenReturn(channel);
		assertThat(channelController).isNotNull();
		setField(channelController, "channelDao", channelDao);
	}

	private void givenGameRepositoryIsMocked() {
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Query query = mock(Query.class);
		when(query.setMaxResults(anyInt())).thenReturn(query);
		when(entityManager.createNamedQuery("Game.findByTitle")).thenReturn(query);
		gameDao = mock(GameRepository.class);
		when(gameDao.findByTitle(anyString())).thenReturn(game);
	}


	@Test
	public void when_saving_channel_then_channel_is_persisted() {
		givenBeanHasBeenValidated();
		channelController.save(channel, bindingResult, new MockHttpServletRequest(), new ModelAndView());
		verify(channelDao).saveOrUpdate(channel);
	}

	@Test
	public void when_saving_is_successful_then_admin_homepageview_is_returned() {
		givenBeanHasBeenValidated();
		ModelAndView modelAndView = channelController.save(channel, bindingResult, new MockHttpServletRequest(), new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("redirect:/admin/home");
	}

	@Test
	public void when_saving_is_unsuccessful_then_homepageview_is_returned() {
		givenBeanHasBeenInvalidated();
		ModelAndView modelAndView = channelController.save(channel, bindingResult, new MockHttpServletRequest(), new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("channel/form");
	}

	private void givenBeanHasBeenInvalidated() {
		channel = new Channel();
		bindingResult = new BeanPropertyBindingResult(channel, "channelCommand");
		validator.validate(channel, bindingResult);
		assertThat(bindingResult.getErrorCount()).isGreaterThan(0);
	}

	private void givenBeanHasBeenValidated() {
		bindingResult = new BeanPropertyBindingResult(channel, "channelCommand");
		validator.validate(channel, bindingResult);
		assertThat(bindingResult.getErrorCount()).isEqualTo(0);
	}

}
