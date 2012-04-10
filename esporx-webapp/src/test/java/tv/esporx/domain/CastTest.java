package tv.esporx.domain;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CastTest {

	private Cast cast;
	private Game game;
	private User user;

	@Before
	public void setup() {
		cast = new Cast();
		user = new User();
		game = new Game();
	}

	@Test
	public void when_assigning_broadcast_date_to_cast_then_broadcast_is_retrieved() {
		Date broadcastDate = new Date();
		cast.setBroadcastDate(broadcastDate);
		assertThat(cast.getBroadcastDate()).isEqualTo(broadcastDate);
	}

	@Test
	public void when_assigning_language_to_cast_then_retrieved() {
		cast.setLanguage("fr");
		assertThat(cast.getLanguage()).isEqualTo("fr");
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_assigning_null_language_to_cast_then_exception() {
		cast.setLanguage(null);
	}

	@Test
	public void when_assigning_caster_to_cast_then_caster_list_size_is_one() {
		cast.addCaster(user);
		assertThat(cast.getCasters()).hasSize(1);
	}

	@Test
	public void when_assigning_caster_to_cast_then_retrieved_caster_is_the_same() {
		cast.addCaster(user);
		assertThat(cast.getCaster(0)).isEqualTo(user);
	}

	@Test
	public void when_creating_cast_then_caster_list_is_empty() {
		assertThat(cast.getUsers()).isEmpty();
	}

	@Test
	public void when_creating_cast_then_title_is_retrieved() {
		cast.setTitle("tata");
		assertThat(cast.getTitle()).isEqualTo("tata");
	}

	@Test
	public void when_creating_cast_then_video_url_is_retrieved() {
		cast.setVideoUrl("http://youtube.com/watch");
		assertThat(cast.getVideoUrl()).isEqualTo("http://youtube.com/watch");
	}

	@Test
	public void when_creating_cast_with_game_then_retrieved_game_is_the_same() {
		cast.setRelatedGame(game);
		assertThat(cast.getRelatedGame()).isEqualTo(game);
	}

	@Test
	public void when_instanciate_event_then_title_is_empty_string() {
		assertThat(cast.getTitle()).hasSize(0);
	}

	@Test
	public void when_instanciate_video_url_then_video_url_is_empty_string() {
		assertThat(cast.getVideoUrl()).hasSize(0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void when_modifying_retrieved_casters_then_throws_exception() {
		List<User> casters = cast.getCasters();
		casters.add(new User());
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_setting_null_game_then_throws_exception() {
		cast.setRelatedGame(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_setting_null_title_then_throws_exception() {
		cast.setTitle(null);
	}
}
