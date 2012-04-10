package tv.esporx.domain;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class EventTest {

	private Cast cast;
	private Event event;

	@Before
	public void setup() {
		event = new Event();
		cast = new Cast();
	}

	@Test
	public void when_adding_beginning_date_then_date_is_retrieved() {
		Date startDate = new Date();
		event.setStartDate(startDate);
		assertThat(event.getStartDate()).isEqualTo(startDate);
	}

	@Test
	public void when_adding_cast_to_event_then_list_size_is_one() {
		event.addCast(cast);
		assertThat(event.getCasts().size()).isEqualTo(1);
	}

	@Test
	public void when_adding_cast_to_event_then_retrieved_cast_is_the_same() {
		event.addCast(cast);
		assertThat(event.getCast(0)).isEqualTo(cast);
	}

	@Test
	public void when_adding_end_date_then_date_is_retrieved() {
		Date endDate = new Date();
		event.setEndDate(endDate);
		assertThat(event.getEndDate()).isEqualTo(endDate);
	}

	@Test
	public void when_assigning_description_then_description_is_retrieved() {
		event.setDescription("This is a description of the event");
		assertThat(event.getDescription()).isEqualTo("This is a description of the event");
	}

	@Test
	public void when_creating_event_then_cast_list_is_empty() {
		assertThat(event.getCasts()).isEmpty();
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
}
