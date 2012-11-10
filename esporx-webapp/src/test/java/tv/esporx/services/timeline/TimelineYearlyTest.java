package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import tv.esporx.domain.Occurrence;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.MONTHLY;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.YEARLY;

public class TimelineYearlyTest extends TimelineTestBase {

    @Before
    public void setUp() {
        super.setUp();
        //this is only allowed in tests
        this.timeline.setMaxMonthsAroundToday(24);
    }

    @Test
    public void should_have_two_slots_when_one_occurrence_repeats_within_the_timeline_range() {
        givenTwoOccurrencesWithAYearlyOne();
        timeline.update(firstStartTime, secondStartTime.plusYears(1));

        assertThat(timeline.allOccurrences()).hasSize(3);

        assertThat(timeline.occurrencesAt(firstStartTime)).containsOnly(//
            occurrenceStartingAt(firstStartTime, YEARLY),               //
            occurrenceStartingOnlyOnceAt(secondStartTime)               //
        );
        DateTime nextStart = firstStartTime.plusYears(1);
        assertThat(timeline.occurrencesAt(nextStart)).containsOnly(     //
            // "same" occurrence, one year later                        //
            occurrenceStartingAt(nextStart, YEARLY)                     //
        );
        verify(occurrenceRepository).findAllInRange(                    //
            firstStartTime,                                             //
            secondStartTime.plusYears(1)                                //
        );
    }

    @Test
    public void should_have_one_slot_when_one_occurrence_repeats_OUT_OF_the_timeline_range() {
        givenTwoOccurrencesWithAYearlyOne();
        timeline.update(firstStartTime,                                 //
                firstStartTime.plusMonths(11).plusWeeks(3).plusDays(6)  //
                        .plusHours(23));

        assertThat(timeline.allOccurrences()).hasSize(2);

        assertThat(timeline.occurrencesAt(firstStartTime)).containsOnly(//
            occurrenceStartingAt(firstStartTime, YEARLY),               //
            occurrenceStartingOnlyOnceAt(secondStartTime)               //
        );

        verify(occurrenceRepository).findAllInRange(                    //
            firstStartTime,                                             //
            firstStartTime.plusMonths(11).plusWeeks(3)                  //
                    .plusDays(6).plusHours(23)                          //
        );
    }

    @Test
    public void should_have_13_slots_when_one_monthly_occurrence_in_an_inclusive_year_slot() {
        givenAMonthlyOccurrence();
        timeline.update(firstStartTime, secondStartTime.plusYears(1));

        assertThat(timeline.allOccurrences()).hasSize(13);
        DateTime nextStart = firstStartTime;
        for (int i = 0; i < 13; i++) {
            assertThat(timeline.occurrencesAt(nextStart)).containsOnly(occurrenceStartingAt(nextStart, MONTHLY));
            nextStart = nextStart.plusMonths(1);
        }
        verify(occurrenceRepository).findAllInRange(                    //
            firstStartTime,                                             //
            secondStartTime.plusYears(1)                                //
        );
    }

    private void givenAMonthlyOccurrence() {
        Occurrence firstOccurrence = occurrenceStartingAt(firstStartTime, MONTHLY);
        when(occurrenceRepository.findAllInRange(                       //
            firstStartTime,                                             //
            secondStartTime.plusYears(1)                                //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
                firstOccurrence                                             //
        ));
    }

    protected void givenTwoOccurrencesWithAYearlyOne() {
        Occurrence firstOccurrence = occurrenceStartingAt(firstStartTime, YEARLY);
        Occurrence secondOccurrence = occurrenceStartingOnlyOnceAt(secondStartTime);

        when(occurrenceRepository.findAllInRange(                       //
            firstStartTime,                                             //
            secondStartTime.plusYears(1)                                //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
                firstOccurrence,                                            //
                secondOccurrence                                            //
        ));

        when(occurrenceRepository.findAllInRange(                       //
            firstStartTime,                                             //
            firstStartTime.plusMonths(11).plusWeeks(3)                  //
                .plusDays(6).plusHours(23)                              //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
                firstOccurrence,                                            //
                secondOccurrence                                            //
        ));
    }


}
