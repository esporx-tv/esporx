package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.junit.Test;
import tv.esporx.domain.Occurrence;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.MONTHLY;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.WEEKLY;

public class TimelineMonthlyTest extends TimelineTest {
    @Test
    public void should_have_two_slots_when_one_occurrence_repeats_within_the_timeline_range() {
        givenTwoOccurrencesWithAMonthlyOne();
        timeline.update(firstStartTime, secondStartTime.plusMonths(1));

        assertThat(timeline.allOccurrences()).hasSize(3);

        assertThat(timeline.occurrencesAt(firstStartTime)).containsOnly(          //
            occurrenceStartingAt(firstStartTime, MONTHLY),              //
            occurrenceStartingOnlyOnceAt(secondStartTime)               //
        );
        DateTime nextStart = firstStartTime.plusMonths(1);
        assertThat(timeline.occurrencesAt(nextStart)).containsOnly(               //
            // "same" occurrence, one month later                       //
            occurrenceStartingAt(nextStart, MONTHLY)                    //
        );
        verify(occurrenceRepository).findAllInRange(                    //
            firstStartTime,                                             //
            secondStartTime.plusMonths(1)                               //
        );
    }

    @Test
    public void should_have_one_slot_when_one_occurrence_repeats_OUT_OF_the_timeline_range() {
        givenTwoOccurrencesWithAMonthlyOne();
        timeline.update(
                firstStartTime,                                         //
                firstStartTime.plusWeeks(3).plusDays(6).plusHours(23)   //
        );

        assertThat(timeline.allOccurrences()).hasSize(2);

        assertThat(timeline.occurrencesAt(firstStartTime)).containsOnly(          //
            occurrenceStartingAt(firstStartTime, MONTHLY),              //
            occurrenceStartingOnlyOnceAt(secondStartTime)               //
        );

        verify(occurrenceRepository).findAllInRange(                    //
            firstStartTime,                                 //
            firstStartTime.plusWeeks(3).plusDays(6).plusHours(23)                                     //
        );
    }

    @Test
    public void should_have_5_slots_when_one_weekly_occurrence_in_an_inclusive_month_slot() {
        givenAWeeklyOccurrence();
        timeline.update(firstStartTime, secondStartTime.plusMonths(1));

        assertThat(timeline.allOccurrences()).hasSize(5);
        DateTime nextStart = firstStartTime;
        for (int i = 0; i < 5; i++) {
            assertThat(timeline.occurrencesAt(nextStart)).containsOnly(occurrenceStartingAt(nextStart, WEEKLY));
            nextStart = nextStart.plusWeeks(1);
        }
        verify(occurrenceRepository).findAllInRange(                    //
                firstStartTime,                                         //
                secondStartTime.plusMonths(1)                           //
        );
    }

    private void givenAWeeklyOccurrence() {
        Occurrence firstOccurrence = occurrenceStartingAt(firstStartTime, WEEKLY);
        when(occurrenceRepository.findAllInRange(                       //
            firstStartTime,                                             //
            secondStartTime.plusMonths(1)                               //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
            firstOccurrence                                             //
        ));
    }

    protected void givenTwoOccurrencesWithAMonthlyOne() {
        Occurrence firstOccurrence = occurrenceStartingAt(firstStartTime, MONTHLY);
        Occurrence secondOccurrence = occurrenceStartingOnlyOnceAt(secondStartTime);

        when(occurrenceRepository.findAllInRange(                       //
            firstStartTime,                                             //
            secondStartTime.plusMonths(1)                               //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
            firstOccurrence,                                            //
            secondOccurrence                                            //
        ));

        when(occurrenceRepository.findAllInRange(                       //
            firstStartTime,                                             //
            firstStartTime.plusWeeks(3)                                 //
                    .plusDays(6).plusHours(23)                          //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
            firstOccurrence,                                            //
            secondOccurrence                                            //
        ));
    }
}
