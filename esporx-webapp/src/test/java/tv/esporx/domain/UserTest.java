package tv.esporx.domain;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

	private User user;

	@Before
	public void setup() {
		user = new User();
	}

	@Test
	public void when_assigning_email_to_user_then_retrieved_email_is_the_same() {
		user.setEmail("tata@gmail.com");
		assertThat(user.getEmail()).isEqualTo("tata@gmail.com");
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_assigning_email_with_invalid_syntax_to_user_then_throws_exception() {
		user.setEmail("tatagmail.com");
	}

	@Test
	public void when_assigning_first_name_to_user_then_retrieved_first_name_is_the_same() throws Exception {
		user.setFirstName("Patrick");
		assertThat(user.getFirstName()).isEqualTo("Patrick");
	}

	@Test
	public void when_assigning_last_name_to_user_then_retrieved_last_name_is_the_same() throws Exception {
		user.setLastName("Patrick");
		assertThat(user.getLastName()).isEqualTo("Patrick");
	}

	@Test
	public void when_assigning_nickname_to_user_then_retrieved_nickname_is_the_same() {
		user.setNickname("tata");
		assertThat(user.getNickname()).isEqualTo("tata");
	}

	@Test
	public void when_assigning_password_to_user_then_retrieved_password_is_the_same() {
		user.setPassword("s3cr3t");
		assertThat(user.getPassword()).isEqualTo("s3cr3t");
	}

	@Test
	public void when_creating_user_then_email_is_empty_string() {
		assertThat(user.getEmail()).isEmpty();
	}

	@Test
	public void when_creating_user_then_first_name_is_empty_string() {
		assertThat(user.getFirstName()).isEmpty();
	}

	@Test
	public void when_creating_user_then_last_name_is_empty_string() {
		assertThat(user.getLastName()).isEmpty();
	}

	@Test
	public void when_creating_user_then_nickname_is_empty_string() {
		assertThat(user.getNickname()).isEmpty();
	}

	@Test
	public void when_creating_user_then_password_is_empty_string() {
		assertThat(user.getPassword()).isEmpty();
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_setting_null_email_then_throws_exception() {
		user.setEmail(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_setting_null_first_name_then_throws_exception() {
		user.setFirstName(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_setting_null_last_name_then_throws_exception() {
		user.setLastName(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_setting_null_nickname_then_throws_exception() {
		user.setNickname(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_setting_null_password_then_throws_exception() {
		user.setPassword(null);
	}
}
