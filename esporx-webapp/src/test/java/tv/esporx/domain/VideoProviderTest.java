package tv.esporx.domain;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class VideoProviderTest {

	private VideoProvider provider;

	@Before
	public void setup() {
		provider = new VideoProvider();
		provider.setPattern("http:\\/\\/www\\.dummy.com\\/\\?id=(\\d{2})");
		provider.setTemplate("<object>{ID}</object>");
	}

	@Test
	public void when_setting_valid_pattern_then_pattern_is_retrieved() {
		assertThat(provider.getPattern()).isEqualTo("http:\\/\\/www\\.dummy.com\\/\\?id=(\\d{2})");
	}

	@Test
	public void when_assigning_template_then_template_is_retrieved() {
		assertThat(provider.getTemplate()).isEqualTo("<object>{ID}</object>");
	}

	@Test
	public void when_requesting_embedded_contents_with_matching_url_then_content_is_retrieved() {
		assertThat(provider.getContents("http://www.dummy.com/?id=26")).isEqualTo("<object>26</object>");
	}

    @Test
    public void when_channel_url_and_provider_then_channel_name_is_extracted() {
        assertThat(provider.extractChannelName("http://www.dummy.com/?id=26")).isEqualTo("26");
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_channel_url_and_non_matching_provider_then_exception() {
        provider.extractChannelName("http://www.dummmmmmmmmmmmmmy.com/?id=26");
    }
}
