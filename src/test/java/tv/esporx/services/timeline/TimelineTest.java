package tv.esporx.services.timeline;

import com.google.common.collect.Multimap;
import org.joda.time.DateTime;
import org.junit.Test;
import tv.esporx.domain.Occurrence;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;
import static tv.esporx.framework.time.DateTimeUtils.toStartDay;
import static tv.esporx.framework.time.DateTimeUtils.toStartHour;

public class TimelineTest extends TimelineTestBase {

    @Test
    public void should_be_empty_when_no_occurrences() {
        givenNoFoundOccurrences();

        assertThat(timeline.getTimeline(firstStartTime, secondStartTime).perHourMap()).isEmpty();
        verify(occurrenceRepository).findAllInRange(
            firstStartTime,
            secondStartTime
        );
    }

    @Test
    public void should_sort_time_slots_chronologically() {
        secondStartTime = firstStartTime.plusHours(1);
        givenTwoOccurrencesIncludingADailyOne();

        Set<Map.Entry<DateTime,Map<DateTime,Collection<Occurrence>>>> entries = timeline.getTimeline(firstStartTime, firstStartTime.plusHours(23)).perDayAndPerHourMap().entrySet();

        assertThat(entries).hasSize(2);
        Iterator<Map.Entry<DateTime,Map<DateTime,Collection<Occurrence>>>> iterator = entries.iterator();
        Map.Entry<DateTime, Map<DateTime, Collection<Occurrence>>> firstDay = iterator.next();
        assertThat(firstDay.getKey()).isEqualTo(toStartDay(firstStartTime));
        assertThat(iterator.next().getKey()).isEqualTo(toStartDay(firstStartTime).plusDays(1));

        Map<DateTime, Collection<Occurrence>> daySlots = firstDay.getValue();
        assertThat(daySlots.keySet()).hasSize(2);
        assertThat(daySlots.keySet().iterator().next()).isEqualTo(toStartHour(firstStartTime));

    }

    @Test
    public void should_have_one_slot_when_all_occurrences_occur_once_and_start_in_same_time_slot() {
        givenTwoOccurrencesOccurringOnceInTheSameTimeSlot();

        Multimap<DateTime,Occurrence> map = this.timeline.getTimeline(firstStartTime, secondStartTime).perHourMultimap();

        assertThat(map.values()).hasSize(2);
        assertThat(map.get(toStartHour(firstStartTime))).containsOnly(
            occurrenceStartingOnlyOnceAt(firstStartTime),
            occurrenceStartingOnlyOnceAt(secondStartTime)
        );
        verify(occurrenceRepository).findAllInRange(
            firstStartTime,
            secondStartTime
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

    @Test(expected = NullPointerException.class)
    public void should_throw_error_if_null_start() {
        timeline.getTimeline(null, new DateTime());
    }

    @Test(expected = NullPointerException.class)
    public void should_throw_error_if_null_end() {
        timeline.getTimeline(null, new DateTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_error_if_start_after_end() {
        timeline.getTimeline(new DateTime().plusDays(2), new DateTime());
    }
}
