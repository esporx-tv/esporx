package tv.esporx.framework.string;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class MarkupKillerTest {

	private MarkupKiller markupKiller;

	@Before
	public void before() {
		markupKiller = new MarkupKiller();
	}

	@Test
	public void when_plain_text_then_equal_text_returned() {
		assertThat(markupKiller.stripTags("plain")).isEqualTo("plain");
	}

	@Test
	public void when_unbalanced_html_then_markup_removed() {
		assertThat(markupKiller.stripTags("<b>plain")).isEqualTo("plain");
		assertThat(markupKiller.stripTags("<b id='whatever'>plain<img />")).isEqualTo("plain");
	}

	@Test
	public void when_balanced_html_then_markup_removed() {
		assertThat(markupKiller.stripTags("<b>plain</b>")).isEqualTo("plain");
		assertThat(markupKiller.stripTags("<b id='whatever'>plain")).isEqualTo("plain");
	}

	@Test
	@Ignore("not supported and might be not needed")
	public void when_balanced_html_entities_then_markup_removed() {
		assertThat(markupKiller.stripTags("&lt;b&gt;plain</b>")).isEqualTo("plain");
		assertThat(markupKiller.stripTags("&lt;b id='whatever'&gt;plain")).isEqualTo("plain");
	}

}
