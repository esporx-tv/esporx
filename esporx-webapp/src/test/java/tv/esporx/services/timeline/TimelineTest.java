package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;
import tv.esporx.domain.FrequencyType;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.OccurrenceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.ONCE;

public class TimelineTest extends TimelineBaseTest {

    @Test
    public void should_be_empty_when_no_occurrences() {
        givenNoFoundOccurrences();

        timeline.update(firstStartTime, secondStartTime);

        assertThat(timeline.allOccurrences()).isEmpty();
        verify(occurrenceRepository).findAllInRange(
            firstStartTime,
            secondStartTime
        );
    }

    @Test
    public void should_have_one_slot_when_all_occurrences_occur_once_and_start_in_same_time_slot() {
        givenTwoOccurrencesOccurringOnceInTheSameTimeSlot();
        timeline.update(firstStartTime, secondStartTime);

        assertThat(timeline.allOccurrences()).hasSize(2);
        assertThat(timeline.occurrencesAt(firstStartTime)).containsOnly(
            occurrenceStartingOnlyOnceAt(firstStartTime),
            occurrenceStartingOnlyOnceAt(secondStartTime)
        );
        verify(occurrenceRepository).findAllInRange(
            firstStartTime,
            secondStartTime
        );
    }

    @Test
    public void should_have_two_slots_when_one_occurrence_repeats_within_the_timeline_range() {
        givenTwoOccurrencesWithADailyOne();
        timeline.update(firstStartTime, secondStartTime.plusDays(1));

        assertThat(timeline.allOccurrences()).hasSize(3);

        assertThat(timeline.occurrencesAt(firstStartTime)).containsOnly(
            occurrenceStartingAt(firstStartTime, DAILY),
            occurrenceStartingOnlyOnceAt(secondStartTime)
        );

        DateTime nextDayOccurrenceStart = firstStartTime.plusDays(1);
        assertThat(timeline.occurrencesAt(nextDayOccurrenceStart)).containsOnly(
            // "same" occurrence, one day later
            occurrenceStartingAt(nextDayOccurrenceStart, DAILY)
        );
        verify(occurrenceRepository).findAllInRange(
            firstStartTime,
            secondStartTime.plusDays(1)
        );
    }

    @Test
    public void should_have_one_slot_when_one_occurrence_repeats_OUT_OF_the_timeline_range() {
        givenTwoOccurrencesWithADailyOne();
        timeline.update(firstStartTime, firstStartTime.plusHours(23));

        assertThat(timeline.allOccurrences()).hasSize(2);

        assertThat(timeline.occurrencesAt(firstStartTime)).containsOnly(
            occurrenceStartingAt(firstStartTime, DAILY),
            occurrenceStartingOnlyOnceAt(secondStartTime)
        );

        verify(occurrenceRepository).findAllInRange(
            firstStartTime,
            firstStartTime.plusHours(23)
        );
    }

    @Test(expected = NullPointerException.class)
    public void should_throw_error_if_null_start() {
        timeline.occurrencesBetween(null, new DateTime());
    }

    @Test(expected = NullPointerException.class)
    public void should_throw_error_if_null_end() {
        timeline.occurrencesBetween(null, new DateTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_error_if_start_after_end() {
        timeline.occurrencesBetween(new DateTime().plusDays(2), new DateTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_error_if_interval_is_too_broad() {
        timeline.occurrencesBetween(new DateTime().minusMonths(3), new DateTime());
    }

    @Test
    public void should_find_matching_occurrences_in_interval() {
        givenTwoOccurrencesWithADailyOne();
        timeline.update(firstStartTime, firstStartTime.plusHours(23));

        Set<Occurrence> occurrences = timeline.occurrencesBetween(firstStartTime.minusHours(1), firstStartTime.plusHours(2));

        assertThat(occurrences).hasSize(2);
        assertThat(occurrences).containsOnly(
            occurrenceStartingAt(firstStartTime, DAILY),
            occurrenceStartingOnlyOnceAt(secondStartTime)
        );

        verify(occurrenceRepository).findAllInRange(
                firstStartTime,
                firstStartTime.plusHours(23)
        );
    }
}
