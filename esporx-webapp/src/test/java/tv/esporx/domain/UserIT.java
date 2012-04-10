package tv.esporx.domain;

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
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml", "classpath:/META-INF/spring/testApplicationContext.xml" })
public class UserIT {

	private User user;
	@Autowired
	private Validator validator;

	@Before
	public void setup() {
		user = new User();
	}

	@Test
	public void when_user_email_has_more_than_255_then_it_is_invalid() {
		user.setEmail("Toto@Roxx.fr");
		Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "email");
		assertThat(violations).hasSize(0);
		user.setEmail(generateString(255) + "@email.com");
		violations = validator.validateProperty(user, "email");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_user_firstname_has_more_than_100_then_it_is_invalid() {
		user.setFirstName("Toto Roxx");
		Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "firstName");
		assertThat(violations).hasSize(0);
		user.setFirstName(generateString(101));
		violations = validator.validateProperty(user, "firstName");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_user_has_empty_email_then_it_is_invalid() {
		Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "email");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_user_has_empty_firstname_then_it_is_invalid() {
		Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "firstName");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_user_has_empty_lastname_then_it_is_invalid() {
		Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "lastName");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_user_has_empty_nickname_then_it_is_invalid() {
		Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "nickname");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_user_has_password_longer_than_30_then_it_is_invalid() {
		user.setPassword("Toto Roxx");
		Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "password");
		assertThat(violations).hasSize(0);
		user.setPassword(generateString(31));
		violations = validator.validateProperty(user, "password");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_user_has_password_shorter_than_6_then_it_is_invalid() {
		user.setPassword("azert");
		Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "password");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_user_lastname_has_more_than_100_then_it_is_invalid() {
		user.setLastName("Toto Roxx");
		Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "lastName");
		assertThat(violations).hasSize(0);
		user.setLastName(generateString(101));
		violations = validator.validateProperty(user, "lastName");
		assertThat(violations).hasSize(1);
	}

	@Test
	public void when_user_nickname_has_more_than_100_then_it_is_invalid() {
		user.setNickname("Toto Roxx");
		Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "nickname");
		assertThat(violations).hasSize(0);
		user.setNickname(generateString(101));
		violations = validator.validateProperty(user, "nickname");
		assertThat(violations).hasSize(1);
	}

}
