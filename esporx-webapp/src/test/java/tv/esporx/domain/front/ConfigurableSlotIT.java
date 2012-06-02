package tv.esporx.domain.front;

import static org.fest.assertions.Assertions.assertThat;
import static tv.esporx.framework.StringGeneratorUtils.generateString;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
"classpath:/META-INF/spring/testApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class ConfigurableSlotIT {

	private ConfigurableSlot slot;
	@Autowired
	private Validator validator;

	@Before
	public void setup() {
		slot = new ConfigurableSlot();
	}

	@Test
	public void when_setting_description_longer_than_1000_then_throws_exception() {
		slot.setDescription("This is a description.");
		Set<ConstraintViolation<ConfigurableSlot>> violations = validator.validateProperty(slot, "description");
		assertThat(violations).hasSize(0);
		slot.setDescription(generateString(1001));
		violations = validator.validateProperty(slot, "description");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_setting_link_longer_than_1000_then_throws_exception() {
		slot.setLink("http://www.youtube.com");
		Set<ConstraintViolation<ConfigurableSlot>> violations = validator.validateProperty(slot, "link");
		assertThat(violations).hasSize(0);
		slot.setLink("http://" + generateString(991) + ".com");
		violations = validator.validateProperty(slot, "link");
		assertThat(violations).hasSize(1);
	}


	@Test
	public void when_slot_has_empty_picture_then_it_is_invalid() {
		Set<ConstraintViolation<ConfigurableSlot>> violations = validator.validateProperty(slot, "picture");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_slot_has_empty_title_then_it_is_invalid() {
		Set<ConstraintViolation<ConfigurableSlot>> violations = validator.validateProperty(slot, "title");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_slot_has_title_longer_than_100_then_it_is_invalid() {
		slot.setTitle("Toto Roxx");
		slot.setBoxTitle("Box title");
		Set<ConstraintViolation<ConfigurableSlot>> violations = validator.validateProperty(slot, "title");
		assertThat(violations).hasSize(0);
		slot.setTitle(generateString(101));
		violations = validator.validateProperty(slot, "title");
		assertThat(violations).hasSize(1);
	}

}
