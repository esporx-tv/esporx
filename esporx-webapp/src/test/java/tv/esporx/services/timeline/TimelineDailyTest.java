package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;

public class TimelineDailyTest extends TimelineTest {

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
}
