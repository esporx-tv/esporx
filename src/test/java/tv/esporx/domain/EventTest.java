package tv.esporx.domain;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class EventTest {

	private Event event;

	@Before
	public void setup() {
		event = new Event();
	}

	@Test
	public void when_assigning_description_then_description_is_retrieved() {
		event.setDescription("This is a description of the event");
		assertThat(event.getDescription()).isEqualTo("This is a description of the event");
	}

	@Test
	public void when_creating_event_then_title_is_retrieved() {
		event.setTitle("Event");
		assertThat(event.getTitle()).isEqualTo("Event");
	}

	@Test
	public void when_instanciate_event_then_description_is_empty_string() {
		assertThat(event.getDescription()).hasSize(0);
	}

	@Test
	public void when_instanciate_event_then_title_is_empty_string() {
		assertThat(event.getTitle()).hasSize(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_setting_null_description_then_throws_exception() {
		event.setDescription(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void when_setting_null_title_then_throws_exception() {
		event.setTitle(null);
	}

    @Test
    public void should_return_empty_twitter_search_query_when_both_id_and_tags_are_null() {
        event.setTwitterId(null);
        event.setTwitterHashtags(null);
        assertThat(event.getTwitterSearch()).isEqualTo("");
    }
}
