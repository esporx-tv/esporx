package tv.esporx.services;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import tv.esporx.dao.PersistenceCapableCast;
import tv.esporx.domain.Cast;
import tv.esporx.domain.Event;
import tv.esporx.domain.front.Timeline;
import tv.esporx.domain.front.TimelineColumn;
import tv.esporx.domain.front.TimelineDimensions;
import tv.esporx.domain.front.TimelineRow;

public class TimelineServiceTest {

	private PersistenceCapableCast castDao;
	private TimelineService underTest;
	private DateTime currentDate;
	private TimelineDimensions timelineDimensions;

	@Before
	public void setup() {
		timelineDimensions = new TimelineDimensions();
		currentDate = new DateTime().withDate(2012, 3, 4).withTime(3, 5, 0, 0);
		castDao = mock(PersistenceCapableCast.class);
		underTest = new TimelineService();
		setField(underTest, "castDao", castDao);
	}

	@Test
	public void when_no_event_found_then_timeline_is_empty() {
		givenNoEventFound();
		Timeline timeline = underTest.buildTimeline(currentDate, timelineDimensions);
		assertThat(timeline.getColumns()).isEmpty();
	}

	@Test
	public void when_no_casts_found_then_timeline_is_empty() {
		givenNoRelatedCastsFound();
		Timeline timeline = underTest.buildTimeline(currentDate, timelineDimensions);
		assertThat(timeline.getColumns()).isEmpty();
	}

	/*
	 * there should be only 1 event slot in this column because the 2 casts
	 * belong to the same event and their broadcast date are comprised within
	 * the row scan interval
	 */
	@Test
	public void when_1_event_with_casts_within_timelime_row_scan_time_window_then_timeline_has_only_1_event_slot() {
		givenOneEventWithGroupedCasts(minutesInScanInterval());
		Timeline timeline = underTest.buildTimeline(currentDate, timelineDimensions);
		assertThat(timeline.getColumns().size()).isEqualTo(1);
		TimelineColumn column = timeline.getColumns().get(0);
		assertThat(column.getRows()).satisfies(new ExpectedRowsWithSlotsCondition(1, 1));
	}

	/*
	 * there should be only 1 event slot in this column because the 2 casts
	 * belong to the same event and their broadcast date are comprised within
	 * the same slot
	 */
	@Test
	public void when_1_event_with_casts_within_timelime_slot_time_window_then_timeline_has_only_1_event_slot() {
		givenOneEventWithGroupedCasts(minutesOutOfScanInterval());
		Timeline timeline = underTest.buildTimeline(currentDate, timelineDimensions);
		assertThat(timeline.getColumns().size()).isEqualTo(1);
		TimelineColumn column = timeline.getColumns().get(0);
		assertThat(column.getRows()).satisfies(new ExpectedRowsWithSlotsCondition(1, 1));
	}

	@Test
	public void when_1_event_with_casts_NOT_within_timelime_slot_time_window_then_timeline_has_2_event_slot() {
		givenOneEventWithGroupedCasts(minutesNotInSlotInterval());
		Timeline timeline = underTest.buildTimeline(currentDate, timelineDimensions);
		assertThat(timeline.getColumns().size()).isEqualTo(1);
		TimelineColumn column = timeline.getColumns().get(0);
		assertThat(column.getRows()).satisfies(new ExpectedRowsWithSlotsCondition(2, 1));
	}

	@Test
	public void when_2_events_with_casts_within_the_row_scan_interval_then_one_row_with_two_slots() {
		givenTwoEventsWithGroupedCasts(minutesInScanInterval());
		Timeline timeline = underTest.buildTimeline(currentDate, timelineDimensions);
		assertThat(timeline.getColumns().size()).isEqualTo(1);
		TimelineColumn column = timeline.getColumns().get(0);
		ExpectedRowsWithSlotsCondition rowCondition = new ExpectedRowsWithSlotsCondition(1, 2);
		assertThat(column.getRows()).satisfies(rowCondition);
		List<TimelineRow> matchingRows = rowCondition.getMatchingRows();
		assertThat(matchingRows.get(0).getSlots().size()).isEqualTo(2);
	}

	@Test
	public void when_2_events_with_casts_NOT_within_the_row_scan_interval_then_two_rows_with_one_slot_each() {
		givenTwoEventsWithGroupedCasts(minutesOutOfScanInterval());
		Timeline timeline = underTest.buildTimeline(currentDate, timelineDimensions);
		assertThat(timeline.getColumns().size()).isEqualTo(1);
		TimelineColumn column = timeline.getColumns().get(0);
		ExpectedRowsWithSlotsCondition rowCondition = new ExpectedRowsWithSlotsCondition(2, 1);
		assertThat(column.getRows()).satisfies(rowCondition);
		List<TimelineRow> matchingRows = rowCondition.getMatchingRows();
		assertThat(matchingRows.get(0).getSlots().size()).isEqualTo(1);
		assertThat(matchingRows.get(1).getSlots().size()).isEqualTo(1);
	}

	@Test
	public void when_1_event_with_2_casts_on_different_days_then_2_columns() {
		givenOneEventWithGroupedCasts(24 * 60);
		Timeline timeline = underTest.buildTimeline(currentDate, timelineDimensions);
		assertThat(timeline.getColumns().size()).isEqualTo(2);
		TimelineColumn column = timeline.getColumns().get(0);
		assertThat(column.getRows()).satisfies(new ExpectedRowsWithSlotsCondition(1, 1));
		TimelineColumn column2 = timeline.getColumns().get(1);
		assertThat(column2.getRows()).satisfies(new ExpectedRowsWithSlotsCondition(1, 1));

	}

	private int minutesInScanInterval() {
		return timelineDimensions.getRowInterval().asMilliseconds() / 2 / 60000;
	}

	private int minutesNotInSlotInterval() {
		return 10 + timelineDimensions.getMaxSlotHeight().asMilliseconds() / 60000;
	}

	private int minutesOutOfScanInterval() {
		return 10 + timelineDimensions.getRowInterval().asMilliseconds() / 60000;
	}

	private void givenOneEventWithGroupedCasts(final int minutes) {
		Cast cast = cast("fr", "Cast One", "http://www.whatever.com", 0);
		Cast cast2 = cast("fr", "Cast Two", "http://www.whateverbis.com", minutes);

		Event event = new Event();
		event.setTitle("Beijing power");
		event.setDescription("Deskrypsyon");
		cast.setEvent(event);
		event.addCast(cast);
		cast2.setEvent(event);
		event.addCast(cast2);
		when(castDao.findTimeLine(any(DateTime.class), any(DateTime.class))).thenReturn(asList(new Cast[] { cast, cast2 }));

	}

	private void givenTwoEventsWithGroupedCasts(final int minutes) {
		Cast cast = cast("fr", "Cast One", "http://www.whatever.com", 0);
		Cast cast2 = cast("fr", "Cast Two", "http://www.whateverbis.com", minutes);

		Event event = new Event();
		event.setTitle("Beijing power");
		event.setDescription("Deskrypsyon");
		Event event2 = new Event();
		event2.setTitle("Beijing power 2");
		event2.setDescription("Deskrypsyon bis");
		cast.setEvent(event);
		event.addCast(cast);
		cast2.setEvent(event2);
		event2.addCast(cast2);
		when(castDao.findTimeLine(any(DateTime.class), any(DateTime.class))).thenReturn(asList(new Cast[] { cast, cast2 }));

	}

	private void givenNoRelatedCastsFound() {
		when(castDao.findTimeLine(any(DateTime.class), any(DateTime.class))).thenReturn(asList(new Cast[] {}));
	}

	private void givenNoEventFound() {
		when(castDao.findTimeLine(any(DateTime.class), any(DateTime.class))).thenReturn(asList(new Cast[] { new Cast() }));
	}

	private Cast cast(final String language, final String title, final String url, final int minutes) {
		Cast cast = new Cast();
		cast.setLanguage(language);
		cast.setTitle(title);
		cast.setVideoUrl(url);
		cast.setBroadcastDate(currentDate.plusDays(1).plusMinutes(minutes).toDate());
		return cast;
	}

}
