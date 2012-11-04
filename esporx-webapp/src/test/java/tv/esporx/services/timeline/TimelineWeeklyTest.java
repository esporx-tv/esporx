package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.junit.Test;
import tv.esporx.domain.Occurrence;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.WEEKLY;
import static tv.esporx.framework.time.DateTimeUtils.toEndDay;
import static tv.esporx.framework.time.DateTimeUtils.toStartDay;

public class TimelineWeeklyTest extends TimelineTest {
    
    @Test
    public void should_have_two_slots_when_one_occurrence_repeats_within_the_timeline_range() {
        givenTwoOccurrencesWithAWeeklyOne();
        timeline.update(firstStartTime, secondStartTime.plusWeeks(1));

        assertThat(timeline.getAll()).hasSize(3);

        assertThat(timeline.get(firstStartTime)).containsOnly(          //
            occurrenceStartingAt(firstStartTime, WEEKLY),               //
            occurrenceStartingOnlyOnceAt(secondStartTime)               //
        );
        DateTime nextStart = firstStartTime.plusWeeks(1);
        assertThat(timeline.get(nextStart)).containsOnly(               //
            // "same" occurrence, one week later                        //
            occurrenceStartingAt(nextStart, WEEKLY)                     //
        );
        verify(occurrenceRepository).findAllInRange(                    //
            toStartDay(firstStartTime).toDate(),                        //
            toEndDay(secondStartTime.plusWeeks(1)).toDate()             //
        );
    }

    @Test
    public void should_have_one_slot_when_one_occurrence_repeats_OUT_OF_the_timeline_range() {
        givenTwoOccurrencesWithAWeeklyOne();
        timeline.update(firstStartTime, firstStartTime.plusDays(6).plusHours(23));

        assertThat(timeline.getAll()).hasSize(2);

        assertThat(timeline.get(firstStartTime)).containsOnly(          //
            occurrenceStartingAt(firstStartTime, WEEKLY),               //
            occurrenceStartingOnlyOnceAt(secondStartTime)               //
        );

        verify(occurrenceRepository).findAllInRange(                    //
            toStartDay(firstStartTime).toDate(),                        //
            toEndDay(firstStartTime.plusDays(6).plusHours(23)).toDate() //
        );
    }

    @Test
    public void should_have_8_slots_when_one_daily_occurrence_in_an_inclusive_week_slot() {
        givenADailyOccurrence();
        timeline.update(firstStartTime, secondStartTime.plusWeeks(1));

        assertThat(timeline.getAll()).hasSize(8);
        DateTime nextStart = firstStartTime;
        for(int i = 0; i < 8; i++) {
            assertThat(timeline.get(nextStart)).containsOnly(occurrenceStartingAt(nextStart, DAILY));
            nextStart = nextStart.plusDays(1);
        }
        verify(occurrenceRepository).findAllInRange(                    //
                toStartDay(firstStartTime).toDate(),                    //
                toEndDay(secondStartTime.plusWeeks(1)).toDate()         //
        );
    }

    private void givenADailyOccurrence() {
        Occurrence firstOccurrence = occurrenceStartingAt(firstStartTime, DAILY);
        when(occurrenceRepository.findAllInRange(                       //
            toStartDay(firstStartTime).toDate(),                        //
            toEndDay(secondStartTime.plusWeeks(1)).toDate()             //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
                firstOccurrence                                             //
        ));
    }

    protected void givenTwoOccurrencesWithAWeeklyOne() {
        Occurrence firstOccurrence = occurrenceStartingAt(firstStartTime, WEEKLY);
        Occurrence secondOccurrence = occurrenceStartingOnlyOnceAt(secondStartTime);

        when(occurrenceRepository.findAllInRange(                       //
            toStartDay(firstStartTime).toDate(),                        //
            toEndDay(secondStartTime.plusWeeks(1)).toDate()             //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
            firstOccurrence,                                            //
            secondOccurrence                                            //
        ));
    }
}
