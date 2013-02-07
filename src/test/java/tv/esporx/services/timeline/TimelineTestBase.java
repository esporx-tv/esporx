package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.test.util.ReflectionTestUtils;
import tv.esporx.domain.FrequencyType;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.OccurrenceRepository;
import tv.esporx.services.TimelineService;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.DAILY;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.ONCE;

public class TimelineTestBase {

    protected OccurrenceRepository occurrenceRepository;
    protected TimelineService timeline;
    protected DateTime firstStartTime;
    protected DateTime secondStartTime;

    @BeforeClass
    public static void prepareAll() {
        DateTimeUtils.setCurrentMillisFixed(new DateTime().getMillis());
    }

    @AfterClass
    public static void cleanAll() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Before
    public void setUp() {
        occurrenceRepository = mock(OccurrenceRepository.class);
        timeline = new TimelineService();
        setField(timeline, "occurrenceRepository", occurrenceRepository);
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

    protected void givenTwoOccurrencesIncludingADailyOne() {
        Occurrence firstOccurrence = occurrenceStartingAt(firstStartTime, DAILY);
        Occurrence secondOccurrence = occurrenceStartingOnlyOnceAt(secondStartTime);

        List<Occurrence> occurrences = newArrayList(            //
            firstOccurrence,                                    //
            secondOccurrence                                    //
        );
        when(occurrenceRepository.findAllInRange(               //
            firstStartTime,                                     //
            secondStartTime.plusDays(1).plusMinutes(1)          //
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
