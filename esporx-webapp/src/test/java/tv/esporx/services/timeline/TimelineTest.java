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

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.ONCE;
import static tv.esporx.framework.time.DateTimeUtils.toEndDay;
import static tv.esporx.framework.time.DateTimeUtils.toStartDay;

public class TimelineTest {

    protected OccurrenceRepository occurrenceRepository;
    protected Timeline timeline;
    protected DateTime firstStartTime;
    protected DateTime secondStartTime;

    @Before
    public void setUp() {
        occurrenceRepository = mock(OccurrenceRepository.class);
        timeline = new Timeline(occurrenceRepository);
        firstStartTime = new DateTime().withTime(22, 22, 0, 0);
        secondStartTime = firstStartTime.plusMinutes(20);
    }

    @Test
    public void should_be_empty_when_no_occurrences() {
        givenNoFoundOccurrences();

        timeline.update(firstStartTime, secondStartTime);

        assertThat(timeline.getAll()).isEmpty();
        verify(occurrenceRepository).findAllInRange(
                toStartDay(firstStartTime).toDate(),
                toEndDay(secondStartTime).toDate()
        );
    }

    @Test
    public void should_have_one_slot_when_all_occurrences_occur_once_and_start_in_same_time_slot() {
        givenTwoOccurrencesOccurringOnceInTheSameTimeSlot();
        timeline.update(firstStartTime, secondStartTime);

        assertThat(timeline.getAll()).hasSize(2);
        assertThat(timeline.get(firstStartTime)).containsOnly(
            occurrenceStartingOnlyOnceAt(firstStartTime),
            occurrenceStartingOnlyOnceAt(secondStartTime)
        );
        verify(occurrenceRepository).findAllInRange(
            toStartDay(firstStartTime).toDate(),
            toEndDay(secondStartTime).toDate()
        );
    }

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

    protected void givenNoFoundOccurrences() {
        whenRepositoryInvokedWithTruncatedDates().thenReturn(new ArrayList<Occurrence>());
    }

    protected void givenTwoOccurrencesOccurringOnceInTheSameTimeSlot() {
        Occurrence firstOccurrence = occurrenceStartingOnlyOnceAt(firstStartTime);
        Occurrence secondOccurrence = occurrenceStartingOnlyOnceAt(secondStartTime);
        whenRepositoryInvokedWithTruncatedDates().thenReturn(newArrayList(
                firstOccurrence,
                secondOccurrence
        ));
    }

    protected void givenTwoOccurrencesWithADailyOne() {
        Occurrence firstOccurrence = occurrenceStartingAt(firstStartTime, DAILY);
        Occurrence secondOccurrence = occurrenceStartingOnlyOnceAt(secondStartTime);

        List<Occurrence> occurrences = newArrayList(            //
                firstOccurrence,                                //
                secondOccurrence                                //
        );
        when(occurrenceRepository.findAllInRange(               //
            toStartDay(firstStartTime).toDate(),                //
            toEndDay(secondStartTime.plusDays(1)).toDate()      //
        ))                                                      //
        .thenReturn(occurrences);
    }

    protected Occurrence occurrenceStartingAt(DateTime start, FrequencyType.FrequencyTypes value) {
        Occurrence firstOccurrence = occurrenceStartingOnlyOnceAt(start);
        firstOccurrence.setFrequencyType(new FrequencyType().setValue(value.name()));
        return firstOccurrence;
    }

    protected Occurrence occurrenceStartingOnlyOnceAt(DateTime baseDateTime) {
        Occurrence occurrence = new Occurrence();
        occurrence.setFrequencyType(new FrequencyType().setValue(ONCE.name()));
        occurrence.setStartDate(baseDateTime.toDate());
        return occurrence;
    }

    protected OngoingStubbing<List<Occurrence>> whenRepositoryInvokedWithTruncatedDates() {
        return when(occurrenceRepository.findAllInRange(
            toStartDay(firstStartTime).toDate(),
            toEndDay(secondStartTime).toDate()
        ));
    }
}