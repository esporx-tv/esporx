package tv.esporx.framework.validation;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class TwitterIdValidatorTest {
    @Test
    public void should_return_false_if_doesnt_starts_with_at() {
        boolean valid = new TwitterIdValidator().isValid("toto", null);
        assertThat(valid).isFalse();
    }

    @Test
    public void should_return_false_if_contains_spaces() {
        boolean valid = new TwitterIdValidator().isValid("@to to", null);
        assertThat(valid).isFalse();
    }

    @Test
    public void should_return_false_if_more_than_20_chars() {
        boolean valid = new TwitterIdValidator().isValid("@toto_tata_toto_tata_a", null);
        assertThat(valid).isFalse();
    }

    @Test
    public void should_return_false_if_at_with_no_chars() {
        boolean valid = new TwitterIdValidator().isValid("@", null);
        assertThat(valid).isFalse();
    }

    @Test
    public void should_return_true_if_empty() {
        boolean valid = new TwitterIdValidator().isValid("", null);
        assertThat(valid).isTrue();
    }
}
