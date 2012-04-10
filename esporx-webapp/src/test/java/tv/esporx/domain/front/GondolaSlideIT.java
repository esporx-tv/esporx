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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
	locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
	"classpath:/META-INF/spring/testApplicationContext.xml"})
public class GondolaSlideIT {

	private GondolaSlide gondolaSlide;

	@Autowired
	private Validator validator;

	@Before
	public void setup() {
		gondolaSlide = new GondolaSlide();
	}

	@Test
	public void when_description_has_more_than_1000_then_it_is_invalid() {
		gondolaSlide.setDescription("this is a description");
		Set<ConstraintViolation<GondolaSlide>> violations = validator.validateProperty(gondolaSlide, "description");
		assertThat(violations).hasSize(0);
		gondolaSlide.setDescription(generateString(1001));
		violations = validator.validateProperty(gondolaSlide, "description");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_description_is_null_then_it_is_invalid() {
		Set<ConstraintViolation<GondolaSlide>> violations = validator.validateProperty(gondolaSlide, "description");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_link_has_more_than_1000_then_it_is_invalid() {
		gondolaSlide.setLink("http://www.youtube.com/");
		Set<ConstraintViolation<GondolaSlide>> violations = validator.validateProperty(gondolaSlide, "link");
		assertThat(violations).hasSize(0);
		gondolaSlide.setLink("http://zaaezaebueizaozzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzaaaaaaaaaaaaaaaa" + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + "zaaezaebueizaozzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzaaaaaaaaaaaaaaaa" + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + "zaaezaebueizaozzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzaaaaaaaaaaaaaaaa" + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + "zaaezaebueizaozzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzaaaaaaaaaaaaaaaa" + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + "zaaezaebueizaozzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzaaaaaaaaaaaaaaaa" + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.com");
		violations = validator.validateProperty(gondolaSlide, "link");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_link_is_null_then_it_is_invalid() {
		Set<ConstraintViolation<GondolaSlide>> violations = validator.validateProperty(gondolaSlide, "link");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_picture_is_null_then_it_is_invalid() {
		Set<ConstraintViolation<GondolaSlide>> violations = validator.validateProperty(gondolaSlide, "picture");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_tag_line_has_more_than_255_then_throws_exception() {
		gondolaSlide.setTagLine("Tag Line");
		Set<ConstraintViolation<GondolaSlide>> violations = validator.validateProperty(gondolaSlide, "tagLine");
		assertThat(violations).hasSize(0);
		gondolaSlide.setTagLine(generateString(256));
		violations = validator.validateProperty(gondolaSlide, "tagLine");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_tag_line_is_null_then_it_is_invalid() {
		Set<ConstraintViolation<GondolaSlide>> violations = validator.validateProperty(gondolaSlide, "tagLine");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_title_has_more_than_255_then_throws_exception() {
		gondolaSlide.setTitle("Starcraft 2 roxx");
		Set<ConstraintViolation<GondolaSlide>> violations = validator.validateProperty(gondolaSlide, "title");
		assertThat(violations).hasSize(0);
		gondolaSlide.setTitle(generateString(256));
		violations = validator.validateProperty(gondolaSlide, "title");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_title_is_null_then_it_is_invalid() {
		Set<ConstraintViolation<GondolaSlide>> violations = validator.validateProperty(gondolaSlide, "title");
		assertThat(violations).hasSize(1);
	}

}
