package tv.esporx.framework;

import static org.fest.assertions.Assertions.assertThat;
import static tv.esporx.framework.StringGeneratorUtils.generateString;

import org.junit.Test;

public class StringGeneratorUtilsTest {

	@Test(expected = IllegalArgumentException.class)
	public void when_passing_negative_limit_then_throws_exception() {
		generateString(-10);
	}

	@Test
	public void when_passing_valid_limit_then_matches_returned_string_length() {
		int size = 30;
		String generatedString = generateString(size);
		assertThat(generatedString).hasSize(size);
	}
}
