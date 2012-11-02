package tv.esporx.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;
import static tv.esporx.framework.StringGeneratorUtils.generateString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
"classpath:/META-INF/spring/testApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class ChannelIT {

	private Channel channel;

	@Autowired
	private Validator validator;

	@Before
	public void setup() {
		channel = new Channel();
	}

	@Test
	public void when_channel_has_empty_title_then_invalid() {
		Set<ConstraintViolation<Channel>> violations = validator.validateProperty(channel, "title");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_channel_has_empty_video_url_then_invalid() {
		Set<ConstraintViolation<Channel>> violations = validator.validateProperty(channel, "videoUrl");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_channel_title_has_more_than_255_then_invalid() {
		channel.setTitle("Toto Roxx");
		Set<ConstraintViolation<Channel>> violations = validator.validateProperty(channel, "title");
		assertThat(violations).hasSize(0);
		channel.setTitle(generateString(256));
		violations = validator.validateProperty(channel, "title");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_channel_video_url_as_more_than_1000_then_invalid() {
		channel.setVideoUrl("http://Toto.coxx");
		Set<ConstraintViolation<Channel>> violations = validator.validateProperty(channel, "videoUrl");
		assertThat(violations).hasSize(0);
		channel.setVideoUrl("http://" + generateString(1000) + ".com");
		violations = validator.validateProperty(channel, "videoUrl");
		assertThat(violations).hasSize(1);
	}

}
