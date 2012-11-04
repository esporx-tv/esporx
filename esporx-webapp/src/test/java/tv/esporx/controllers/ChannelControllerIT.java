package tv.esporx.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Game;
import tv.esporx.framework.TestGenericWebXmlContextLoader;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.GameRepository;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
"classpath:/META-INF/spring/testApplicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class ChannelControllerIT {

	private GameRepository gameRepository;
	private ChannelRepository channelRepository;
	private BindingResult bindingResult;
	private Channel channel;
	private Game game;
	@Autowired
	private Validator validator;
	@Autowired
	private ChannelController channelController;

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
		channelRepository = mock(ChannelRepository.class);
		when(channelRepository.findOne(anyLong())).thenReturn(channel);
		assertThat(channelController).isNotNull();
		setField(channelController, "channelRepository", channelRepository);
	}

	private void givenGameRepositoryIsMocked() {
		gameRepository = mock(GameRepository.class);
		when(gameRepository.findByTitle(anyString())).thenReturn(game);
	}


	@Test
	public void when_saving_channel_then_channel_is_persisted() {
		givenBeanHasBeenValidated();
		channelController.save(channel, bindingResult, new ModelAndView());
		verify(channelRepository).save(channel);
	}

	@Test
	public void when_saving_is_successful_then_admin_homepageview_is_returned() {
		givenBeanHasBeenValidated();
		ModelAndView modelAndView = channelController.save(channel, bindingResult, new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("redirect:/admin/home");
	}

	@Test
	public void when_saving_is_unsuccessful_then_homepageview_is_returned() {
		givenBeanHasBeenInvalidated();
		ModelAndView modelAndView = channelController.save(channel, bindingResult, new ModelAndView());
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
