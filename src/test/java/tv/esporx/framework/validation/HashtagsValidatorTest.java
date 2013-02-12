package tv.esporx.framework.validation;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class HashtagsValidatorTest {

    @Test
    public void should_return_true_if_string_is_empty() {
        boolean valid = new HashtagsValidator().isValid("", null);
        assertThat(valid).isTrue();
    }

    @Test
    public void should_return_false_if_more_than_two_hashtags() {
        boolean valid = new HashtagsValidator().isValid("#one,#two,#three", null);
        assertThat(valid).isFalse();
    }

    @Test
    public void should_return_false_if_hashtag_doesnt_starts_with_croisillon(){
        boolean valid = new HashtagsValidator().isValid("#one,two", null);
        assertThat(valid).isFalse();
    }

    @Test
    public void should_return_false_if_hashtag_is_more_than_20_chars(){
        boolean valid = new HashtagsValidator().isValid("#one,#kqjsdhkjdlkajzdklakajzdjkahzdkjlkjdtwo", null);
        assertThat(valid).isFalse();
    }

    @Test
    public void should_return_true_if_spaces_surrounding_hashtags() {
        boolean valid = new HashtagsValidator().isValid(" #one , #two ", null);
        assertThat(valid).isTrue();
    }

    @Test
    public void should_return_false_hashtag_contains_spaces() {
        boolean valid = new HashtagsValidator().isValid(" #o ne , #two ", null);
        assertThat(valid).isFalse();
    }

    @Test
    public void should_return_true_otherwise() {
        boolean valid = new HashtagsValidator().isValid("#one,#two", null);
        assertThat(valid).isTrue();
    }

    @Test
    public void should_return_true_if_one_hashtag() {
        boolean valid = new HashtagsValidator().isValid("#one", null);
        assertThat(valid).isTrue();
    }
}
