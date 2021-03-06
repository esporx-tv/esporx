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
public class GameIT {

	private Game game;
	@Autowired
	private Validator validator;

	@Before
	public void setup() {
		game = new Game();
	}

	@Test
	public void when_game_has_description_longer_than_1000_then_it_is_invalid() {
		game.setDescription("Toto Roxx");
		Set<ConstraintViolation<Game>> violations = validator.validateProperty(game, "description");
		assertThat(violations).hasSize(0);
		game.setDescription(generateString(1010));
		violations = validator.validateProperty(game, "description");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_game_has_empty_description_then_it_is_invalid() {
		Set<ConstraintViolation<Game>> violations = validator.validateProperty(game, "description");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_game_has_empty_title_then_it_is_invalid() {
		Set<ConstraintViolation<Game>> violations = validator.validateProperty(game, "title");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_game_has_title_longer_than_100_then_it_is_invalid() {
		game.setTitle("Toto Roxx");
		Set<ConstraintViolation<Game>> violations = validator.validateProperty(game, "title");
		assertThat(violations).hasSize(0);
		game.setTitle(generateString(101));
		violations = validator.validateProperty(game, "title");
		assertThat(violations).hasSize(1);
	}
}
