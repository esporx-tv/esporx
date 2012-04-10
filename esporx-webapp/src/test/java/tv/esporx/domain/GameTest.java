package tv.esporx.domain;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

	private Game game;

	@Before
	public void setup() {
		game = new Game();
	}

	@Test
	public void when_assigning_description_to_game_then_retrieved_description_is_the_same() {
		game.setDescription("This is a huge strategy game");
		assertThat(game.getDescription()).isEqualTo("This is a huge strategy game");
	}

	@Test
	public void when_assigning_title_to_game_then_retrieved_title_is_the_same() {
		game.setTitle("Starcraft");
		assertThat(game.getTitle()).isEqualTo("Starcraft");
	}

	@Test
	public void when_instanciate_game_then_description_is_empty_string() {
		assertThat(game.getDescription()).hasSize(0);
	}

	@Test
	public void when_instanciate_game_then_title_is_empty_string() {
		assertThat(game.getTitle()).hasSize(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_setting_null_description_then_throws_exception() {
		game.setDescription(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_setting_null_title_then_throws_exception() {
		game.setTitle(null);
	}
}
