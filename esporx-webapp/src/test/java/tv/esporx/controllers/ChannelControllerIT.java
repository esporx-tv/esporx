package tv.esporx.controllers;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import javax.sql.DataSource;

import java.sql.Date;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "classpath:esporx-servlet.xml",
	"classpath:applicationContext.xml",
"classpath:/META-INF/spring/testApplicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class ChannelControllerIT {

    @Autowired
	private GameRepository gameRepository;
    @Autowired
	private ChannelRepository channelRepository;
	private BindingResult bindingResult;
	private Channel channel;
	private Game game;
	@Autowired
	private Validator validator;
	@Autowired
	private ChannelController channelController;
    @Autowired
    private DataSource dataSource;

    private static final long ID = 1L;
    private static final Operation DELETE_CHANNELS =
            deleteAllFrom("games", "channels", "video_providers");
    private static final Operation INSERT_GAME =
            insertInto("games")
                    .columns("id", "description", "title")
                    .values(ID, "starcraft2", "Birds are REALLY angry this time")
                    .build();
    private static final Operation INSERT_PROVIDER =
            insertInto("video_providers")
                    .columns("id", "pattern", "template", "endpoint")
                    .values(ID, " ^(?:(?:https?)://)?(?:www.)?youtube.com/watch?(?:.*)v=([A-Za-z0-9._%-]{11}).*", "<iframe width=\"425\" height=\"349\" src=\"https://www.youtube.com/embed/{ID}\" frameborder=\"0\" allowfullscreen></iframe>", null)
                    .build();
    private static final Operation INSERT_CHANNELS =
            sequenceOf(
                    DELETE_CHANNELS,
                    INSERT_GAME,
                    INSERT_PROVIDER,
                    insertInto("channels")
                            .columns("id", "description", "language", "title", "video_url", "viewer_count", "provider", "viewer_count_timestamp")
                            .values(ID, "tatata", "en", "TeH channel", "http://not.what.you.think.of", 1337, ID, new Date(new DateTime().getMillis()))
                            .build()
            );

	@Before
	public void setup() {
        new DbSetup(new DataSourceDestination(dataSource), INSERT_CHANNELS).launch();
        channel = channelRepository.findOne(ID);
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
