package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;
import static tv.esporx.framework.time.DateTimeUtils.toEndDay;
import static tv.esporx.framework.time.DateTimeUtils.toStartDay;

public class TimelineDailyTest extends TimelineTest {

    @Test
    public void should_have_two_slots_when_one_occurrence_repeats_within_the_timeline_range() {
        givenTwoOccurrencesWithADailyOne();
        timeline.update(firstStartTime, secondStartTime.plusDays(1));

        assertThat(timeline.getAll()).hasSize(3);

        assertThat(timeline.get(firstStartTime)).containsOnly(
                occurrenceStartingAt(firstStartTime, DAILY),
                occurrenceStartingOnlyOnceAt(secondStartTime)
        );

        DateTime nextDayOccurrenceStart = firstStartTime.plusDays(1);
        assertThat(timeline.get(nextDayOccurrenceStart)).containsOnly(
                // "same" occurrence, one day later
                occurrenceStartingAt(nextDayOccurrenceStart, DAILY)
        );
        verify(occurrenceRepository).findAllInRange(
                toStartDay(firstStartTime).toDate(),
                toEndDay(secondStartTime.plusDays(1)).toDate()
        );
    }

    @Test
    public void should_have_one_slot_when_one_occurrence_repeats_OUT_OF_the_timeline_range() {
        givenTwoOccurrencesWithADailyOne();
        timeline.update(firstStartTime, firstStartTime.plusHours(23));

        assertThat(timeline.getAll()).hasSize(2);

        assertThat(timeline.get(firstStartTime)).containsOnly(
                occurrenceStartingAt(firstStartTime, DAILY),
                occurrenceStartingOnlyOnceAt(secondStartTime)
        );

        verify(occurrenceRepository).findAllInRange(
                toStartDay(firstStartTime).toDate(),
                toEndDay(firstStartTime.plusHours(23)).toDate()
        );
    }
}
