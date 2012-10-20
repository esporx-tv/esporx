package tv.esporx.domain;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ChannelTest {

	private Channel channel;

	@Before
	public void setup() {
		channel = new Channel();
	}

	@Test
	public void when_assigning_language_to_channel_then_retrieved() {
		channel.setLanguage("fr");
		assertThat(channel.getLanguage()).isEqualTo("fr");
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_assigning_null_language_to_channel_then_exception() {
		channel.setLanguage(null);
	}

	@Test
	public void when_creating_channel_then_title_is_retrieved() {
		channel.setTitle("tata");
		assertThat(channel.getTitle()).isEqualTo("tata");
	}

	@Test
	public void when_creating_channel_then_video_url_is_retrieved() {
		channel.setVideoUrl("http://youtube.com/watch");
		assertThat(channel.getVideoUrl()).isEqualTo("http://youtube.com/watch");
	}

	@Test
	public void when_instanciate_event_then_title_is_empty_string() {
		assertThat(channel.getTitle()).hasSize(0);
	}

	@Test
	public void when_instanciate_video_url_then_video_url_is_empty_string() {
		assertThat(channel.getVideoUrl()).hasSize(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_setting_null_title_then_throws_exception() {
		channel.setTitle(null);
	}
}
