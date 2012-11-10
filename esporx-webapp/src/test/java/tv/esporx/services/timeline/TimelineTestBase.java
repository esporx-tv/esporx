package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;
import tv.esporx.domain.FrequencyType;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.OccurrenceRepository;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.ONCE;

public class TimelineTestBase {

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
            firstOccurrence,                                    //
            secondOccurrence                                    //
        );
        when(occurrenceRepository.findAllInRange(               //
            firstStartTime,                                     //
            secondStartTime.plusDays(1)                         //
        ))                                                      //
                .thenReturn(occurrences);
        when(occurrenceRepository.findAllInRange(               //
            firstStartTime,                                     //
            firstStartTime.plusHours(23)                        //
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
            firstStartTime,
            secondStartTime
        ));
    }
}
