package tv.esporx.services.timeline;

import com.google.common.collect.Multimap;
import org.joda.time.DateTime;
import org.junit.Test;
import tv.esporx.domain.Occurrence;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;
import static tv.esporx.framework.time.DateTimeUtils.toStartHour;

public class TimelineDailyTest extends TimelineTestBase {

    @Test
    public void should_have_two_slots_when_one_occurrence_repeats_within_the_timeline_range() {
        givenTwoOccurrencesIncludingADailyOne();

        Multimap<DateTime,Occurrence> map = timeline.getTimeline(firstStartTime, secondStartTime.plusDays(1).plusMinutes(1)).perHourMultimap();

        assertThat(map.values()).hasSize(3);

        assertThat(map.get(toStartHour(firstStartTime))).containsOnly(
            occurrenceStartingAt(firstStartTime, DAILY),
            occurrenceStartingOnlyOnceAt(secondStartTime)
        );

        DateTime nextDayOccurrenceStart = firstStartTime.plusDays(1);
        assertThat(map.get(toStartHour(nextDayOccurrenceStart))).containsOnly(
            // "same" occurrence, one day later
            occurrenceStartingAt(nextDayOccurrenceStart, DAILY)
        );
        verify(occurrenceRepository).findAllInRange(
                firstStartTime,
                secondStartTime.plusDays(1).plusMinutes(1)
        );
    }

    @Test
    public void should_have_one_slot_when_one_occurrence_repeats_OUT_OF_the_timeline_range() {
        givenTwoOccurrencesIncludingADailyOne();
        Multimap<DateTime, Occurrence> map = timeline.getTimeline(firstStartTime, firstStartTime.plusHours(23)).perHourMultimap();

        assertThat(map.values()).hasSize(2);

        assertThat(map.get(toStartHour(firstStartTime))).containsOnly(
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
        DateTime start = new DateTime().withTime(22, 22, 0, 0);
        DateTime end = start.plusDays(1);
        when(occurrenceRepository.findAllInRange(start, end)).thenReturn(
            newArrayList(
                occurrenceStartingAt(start.minusHours(5), DAILY)
            )
        );

        Collection<Occurrence> occurrences = timeline.getTimeline(start, end).perHourMultimap().values();
        assertThat(occurrences)
            .hasSize(1)
            .contains(occurrenceStartingAt(start.plusHours(24 - 5), DAILY));
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

        Collection<Occurrence> occurrences = timeline.getTimeline(timelineStart, timelineEnd).perHourMultimap().values();
        assertThat(occurrences)
            .hasSize(2)
            .contains(
                occurrenceStartingAt(timelineStart.plusHours(15), DAILY),
                occurrenceStartingAt(timelineStart.plusDays(1).plusHours(15), DAILY)
            );
    }
}
