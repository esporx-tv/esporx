package tv.esporx.domain.front;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ConfigurableSlotTest {

	private ConfigurableSlot slot;

	@Before
	public void setup() {
		slot = new ConfigurableSlot();
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_assigning_invalid_link_then_throws_exception() {
		slot.setLink("toto");
	}

	@Test
	public void when_getting_id_then_must_be_positive() {
		assertThat(slot.getId()).isGreaterThanOrEqualTo(0L);
	}

	@Test
	public void when_instanciating_then_description_is_empty() {
		assertThat(slot.getDescription()).isEmpty();
	}

	@Test
	public void when_instanciating_then_link_is_empty() {
		assertThat(slot.getLink()).isEmpty();
	}

	@Test
	public void when_instanciating_then_picture_is_empty() {
		assertThat(slot.getPicture()).isEmpty();
	}

	@Test
	public void when_instanciating_then_title_is_empty() {
		assertThat(slot.getTitle()).isEmpty();
	}

	@Test
	public void when_setting_description_then_is_retrieved() {
		String description = "This is a description";
		slot.setDescription(description);
		assertThat(slot.getDescription()).isEqualTo(description);
	}

	@Test
	public void when_setting_link_then_is_retrieved() {
		String link = "http://www.google.cn";
		slot.setLink(link);
		assertThat(slot.getLink()).isEqualTo(link);
	}

	@Test(expected = NullPointerException.class)
	public void when_setting_null_description_longer_then_throws_exception() {
		slot.setDescription(null);
	}


	@Test
	public void when_setting_picture_then_is_retrieved() {
		String picture = "toto.png";
		slot.setPicture(picture);
		assertThat(slot.getPicture()).isEqualTo(picture);
	}

	@Test
	public void when_setting_title_then_is_retrieved() {
		String title = "This is a title";
		slot.setTitle(title);
		assertThat(slot.getTitle()).isEqualTo(title);
	}
}
