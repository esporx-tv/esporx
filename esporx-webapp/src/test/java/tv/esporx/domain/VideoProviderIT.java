package tv.esporx.domain;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml", "classpath:/META-INF/spring/testApplicationContext.xml" })
public class VideoProviderIT {

	private final VideoProvider provider = new VideoProvider();

	@Autowired
	private Validator validator;

	@Test
	public void when_empty_pattern_then_invalid() {
		provider.setPattern("");
		Set<ConstraintViolation<VideoProvider>> violations = validator.validateProperty(provider, "pattern");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_empty_template_then_invalid() {
		provider.setTemplate("");
		Set<ConstraintViolation<VideoProvider>> violations = validator.validateProperty(provider, "template");
		assertThat(violations).hasSize(1);
	}
}
