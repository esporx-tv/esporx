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
locations = { "classpath:esporx-servlet.xml",
	"classpath:applicationContext.xml",
"classpath:/META-INF/spring/testApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class EventIT {

	private Event event;
	@Autowired
	private Validator validator;

	@Before
	public void setup() {
		event = new Event();
	}

	@Test
	public void when_channel_title_has_more_than_255_then_it_is_invalid() {
		event.setTitle("Toto Roxx");
		Set<ConstraintViolation<Event>> violations = validator.validateProperty(event, "title");
		assertThat(violations).hasSize(0);
		event.setTitle(generateString(256));
		violations = validator.validateProperty(event, "title");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_event_has_empty_title_then_it_is_invalid() {
		Set<ConstraintViolation<Event>> violations = validator.validateProperty(event, "title");
		assertThat(violations).hasSize(1);
	}
}
