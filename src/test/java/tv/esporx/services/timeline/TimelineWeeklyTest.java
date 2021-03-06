package tv.esporx.services.timeline;

import com.google.common.collect.Multimap;
import org.joda.time.DateTime;
import org.junit.Test;
import tv.esporx.domain.Occurrence;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.WEEKLY;
import static tv.esporx.framework.time.DateTimeUtils.toStartHour;

public class TimelineWeeklyTest extends TimelineTestBase {
    
    @Test
    public void should_have_two_slots_when_one_occurrence_repeats_within_the_timeline_range() {
        givenTwoOccurrencesWithAWeeklyOne();
        Multimap<DateTime,Occurrence> map = timeline.getTimeline(firstStartTime, secondStartTime.plusWeeks(1)).perHourMultimap();

        assertThat(map.values()).hasSize(3);

        assertThat(map.get(toStartHour(firstStartTime))).containsOnly(               //
            occurrenceStartingAt(firstStartTime, WEEKLY),               //
            occurrenceStartingOnlyOnceAt(secondStartTime)               //
        );
        DateTime nextStart = firstStartTime.plusWeeks(1);
        assertThat(map.get(toStartHour(nextStart))).containsOnly(               //
            // "same" occurrence, one week later                        //
            occurrenceStartingAt(nextStart, WEEKLY)                     //
        );
        verify(occurrenceRepository).findAllInRange(                    //
            firstStartTime,                                             //
            secondStartTime.plusWeeks(1)                                //
        );
    }

    @Test
    public void should_have_one_slot_when_one_occurrence_repeats_OUT_OF_the_timeline_range() {
        givenTwoOccurrencesWithAWeeklyOne();
        Multimap<DateTime, Occurrence> map = timeline.getTimeline(                                                //
            firstStartTime,                                             //
            firstStartTime.plusDays(6).plusHours(23)                    //
        ).perHourMultimap();

        assertThat(map.values()).hasSize(2);

        assertThat(map.get(toStartHour(firstStartTime))).containsOnly(          //
            occurrenceStartingAt(firstStartTime, WEEKLY),               //
            occurrenceStartingOnlyOnceAt(secondStartTime)               //
        );

        verify(occurrenceRepository).findAllInRange(                    //
            firstStartTime,                                             //
            firstStartTime.plusDays(6).plusHours(23)                    //
        );
    }

    @Test
    public void should_have_8_slots_when_one_daily_occurrence_in_an_inclusive_week_slot() {
        givenADailyOccurrence();
        Multimap<DateTime, Occurrence> map = timeline.getTimeline(firstStartTime, secondStartTime.plusWeeks(1)).perHourMultimap();

        assertThat(map.values()).hasSize(8);
        DateTime nextStart = firstStartTime;
        for(int i = 0; i < 8; i++) {
            assertThat(map.get(toStartHour(nextStart))).containsOnly(occurrenceStartingAt(nextStart, DAILY));
            nextStart = nextStart.plusDays(1);
        }
        verify(occurrenceRepository).findAllInRange(                    //
                firstStartTime,                                         //
                secondStartTime.plusWeeks(1)                            //
        );
    }

    private void givenADailyOccurrence() {
        Occurrence firstOccurrence = occurrenceStartingAt(firstStartTime, DAILY);
        when(occurrenceRepository.findAllInRange(                       //
            firstStartTime,                                             //
            secondStartTime.plusWeeks(1)                                //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
                firstOccurrence                                         //
        ));
    }

    protected void givenTwoOccurrencesWithAWeeklyOne() {
        Occurrence firstOccurrence = occurrenceStartingAt(firstStartTime, WEEKLY);
        Occurrence secondOccurrence = occurrenceStartingOnlyOnceAt(secondStartTime);

        when(occurrenceRepository.findAllInRange(                       //
            firstStartTime,                                             //
            secondStartTime.plusWeeks(1)                                //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
            firstOccurrence,                                            //
            secondOccurrence                                            //
        ));

        when(occurrenceRepository.findAllInRange(                       //
            firstStartTime,                                             //
            firstStartTime.plusDays(6).plusHours(23)                    //
        ))                                                              //
        .thenReturn(newArrayList(                                       //
            firstOccurrence,                                            //
            secondOccurrence                                            //
        ));
    }
}
