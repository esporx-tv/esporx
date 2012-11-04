package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.junit.Test;
import tv.esporx.domain.Occurrence;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.MONTHLY;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.YEARLY;
import static tv.esporx.framework.time.DateTimeUtils.toEndDay;
import static tv.esporx.framework.time.DateTimeUtils.toStartDay;

public class TimelineYearlyTest extends TimelineTest {

    @Test
    public void should_have_two_slots_when_one_occurrence_repeats_within_the_timeline_range() {
        givenTwoOccurrencesWithAYearlyOne();
        timeline.update(firstStartTime, secondStartTime.plusYears(1));

        assertThat(timeline.getAll()).hasSize(3);

        assertThat(timeline.get(firstStartTime)).containsOnly(          //
            occurrenceStartingAt(firstStartTime, YEARLY),               //
            occurrenceStartingOnlyOnceAt(secondStartTime)               //
        );
        DateTime nextStart = firstStartTime.plusYears(1);
        assertThat(timeline.get(nextStart)).containsOnly(               //
            // "same" occurrence, one year later                        //
            occurrenceStartingAt(nextStart, YEARLY)                     //
        );
        verify(occurrenceRepository).findAllInRange(                    //
            toStartDay(firstStartTime).toDate(),                        //
            toEndDay(secondStartTime.plusYears(1)).toDate()             //
        );
    }

    @Test
    public void should_have_one_slot_when_one_occurrence_repeats_OUT_OF_the_timeline_range() {
        givenTwoOccurrencesWithAYearlyOne();
        timeline.update(firstStartTime,                                 //
                firstStartTime.plusMonths(11).plusWeeks(3).plusDays(6)  //
                        .plusHours(23));

        assertThat(timeline.getAll()).hasSize(2);

        assertThat(timeline.get(firstStartTime)).containsOnly(          //
            occurrenceStartingAt(firstStartTime, YEARLY),               //
            occurrenceStartingOnlyOnceAt(secondStartTime)               //
        );

        verify(occurrenceRepository).findAllInRange(                    //
            toStartDay(firstStartTime).toDate(),                        //
            toEndDay(firstStartTime.plusMonths(11).plusWeeks(3)         //
                    .plusDays(6).plusHours(23)).toDate()                //
        );
    }

    @Test
    public void should_have_13_slots_when_one_monthly_occurrence_in_an_inclusive_year_slot() {
        givenAMonthlyOccurrence();
        timeline.update(firstStartTime, secondStartTime.plusYears(1));

        assertThat(timeline.getAll()).hasSize(13);
        DateTime nextStart = firstStartTime;
        for (int i = 0; i < 13; i++) {
            assertThat(timeline.get(nextStart)).containsOnly(occurrenceStartingAt(nextStart, MONTHLY));
            nextStart = nextStart.plusMonths(1);
        }
        verify(occurrenceRepository).findAllInRange(                    //
            toStartDay(firstStartTime).toDate(),                        //
            toEndDay(secondStartTime.plusYears(1)).toDate()             //
        );
    }

    private void givenAMonthlyOccurrence() {
        Occurrence firstOccurrence = occurrenceStartingAt(firstStartTime, MONTHLY);
        when(occurrenceRepository.findAllInRange(                       //
            toStartDay(firstStartTime).toDate(),                        //
            toEndDay(secondStartTime.plusYears(1)).toDate()             //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
            firstOccurrence                                             //
        ));
    }

    protected void givenTwoOccurrencesWithAYearlyOne() {
        Occurrence firstOccurrence = occurrenceStartingAt(firstStartTime, YEARLY);
        Occurrence secondOccurrence = occurrenceStartingOnlyOnceAt(secondStartTime);

        when(occurrenceRepository.findAllInRange(                       //
            toStartDay(firstStartTime).toDate(),                        //
            toEndDay(secondStartTime.plusYears(1)).toDate()             //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
            firstOccurrence,                                            //
            secondOccurrence                                            //
        ));

        when(occurrenceRepository.findAllInRange(                       //
            toStartDay(firstStartTime).toDate(),                        //
            toEndDay(secondStartTime.plusMonths(11).plusWeeks(3)        //
                .plusDays(6).plusHours(23)).toDate()                    //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
            firstOccurrence,                                            //
            secondOccurrence                                            //
        ));
    }


}
