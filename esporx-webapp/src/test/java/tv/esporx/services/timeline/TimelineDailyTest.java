package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.junit.Test;
import tv.esporx.domain.Occurrence;

import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;

public class TimelineDailyTest extends TimelineBaseTest {

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

    @Test
    public void should_remove_occurrences_before_timeline_start() {
        DateTime timelineStart = new DateTime().withTime(22, 22, 0, 0);
        DateTime timelineEnd = timelineStart.plusDays(1);
        when(occurrenceRepository.findAllInRange(timelineStart, timelineEnd)).thenReturn(
                newArrayList(
                        occurrenceStartingAt(timelineStart.minusHours(5), DAILY)
                )
        );

        timeline.update(timelineStart, timelineEnd);
        Set<Occurrence> occurrences = timeline.allOccurrences();
        assertThat(occurrences).hasSize(1).contains(occurrenceStartingAt(timelineStart.plusHours(24 - 5), DAILY));
    }

    @Test
    public void should_contain_two_occurrences() {
        DateTime timelineStart = new DateTime().withTime(0, 0, 0, 0);
        DateTime timelineEnd = timelineStart.plusDays(2);
        when(occurrenceRepository.findAllInRange(timelineStart, timelineEnd)).thenReturn(
                newArrayList(
                        occurrenceStartingAt(timelineStart.plusHours(15), DAILY)
                )
        );

        timeline.update(timelineStart, timelineEnd);
        Set<Occurrence> occurrences = timeline.allOccurrences();
        assertThat(occurrences).hasSize(2).contains(occurrenceStartingAt(timelineStart.plusHours(15), DAILY),occurrenceStartingAt(timelineStart.plusDays(1).plusHours(15), DAILY));
    }
}
