package tv.esporx.services.timeline;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import tv.esporx.collections.predicates.IsRepeatingAtFrequencyFilter;
import tv.esporx.domain.Event;
import tv.esporx.domain.FrequencyType;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.EventRepository;
import tv.esporx.repositories.OccurrenceRepository;
import tv.esporx.services.TimelineService;

import javax.sql.DataSource;

import java.sql.Date;
import java.util.Collection;

import static com.google.common.collect.Iterables.filter;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.fest.assertions.Assertions.assertThat;
import static tv.esporx.domain.FrequencyType.FrequencyTypes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
                "classpath:/META-INF/spring/testApplicationContext.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class TimelineIT {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private OccurrenceRepository occurrenceRepository;
    @Autowired
    private TimelineService timeline;
    @Autowired
    private DataSource dataSource;
    private Event event;

    private static final DateTime timelineStart = new DateTime().withTime(22, 0, 0, 0);
    private static final long EVENT_ID = 1L;
    private static final Operation DELETE_ALL = Operations.deleteAllFrom(
            "occurrences", "frequency_types", "events"
    );
    private static final Operation INSERT_EVENT =  insertInto("events")
            .columns("id", "description", "title", "highlight")
            .values(EVENT_ID, "blablablabla", "Oh yeah title", false)
            .build();
    private static final Operation INSERT_FREQUENCIES = insertInto("frequency_types")
            .columns("value")
            .values("ONCE").values("DAILY").values("WEEKLY").values("MONTHLY").values("YEARLY")
            .build();
    private static final Operation INSERT_OCCURRENCES = sequenceOf(
            DELETE_ALL,
            INSERT_EVENT,
            INSERT_FREQUENCIES,
            insertInto("occurrences")
                    .columns("id", "start_date", "end_date", "frequency_type", "event_id")
                    .values(1, new Date(timelineStart.withTime(22, 11, 0, 0).getMillis()), new Date(timelineStart.withTime(23, 11, 0, 0).getMillis()), "ONCE", EVENT_ID)
                    .values(2, new Date(timelineStart.withTime(22, 12, 0, 0).getMillis()), new Date(timelineStart.withTime(23, 12, 0, 0).getMillis()), "DAILY", EVENT_ID)
                    .values(3, new Date(timelineStart.withTime(22, 13, 0, 0).getMillis()), new Date(timelineStart.withTime(23, 13, 0, 0).getMillis()), "WEEKLY", EVENT_ID)
                    .values(4, new Date(timelineStart.withTime(22, 14, 0, 0).getMillis()), new Date(timelineStart.withTime(23, 14, 0, 0).getMillis()), "MONTHLY", EVENT_ID)
                    .values(5, new Date(timelineStart.withTime(22, 15, 0, 0).getMillis()), new Date(timelineStart.withTime(23, 15, 0, 0).getMillis()), "YEARLY", EVENT_ID)
                    .build()
    );

    @Before
    public void setUp() {
        new DbSetup(new DataSourceDestination(dataSource), INSERT_OCCURRENCES).launch();
        event = eventRepository.findOne(EVENT_ID);
    }

    @Test
    public void should_be_only_occurrence_happening_once() {
        Collection<Occurrence> map = timeline.getTimeline(timelineStart, timelineStart.plusYears(2)).perHourMultimap().values();
        Iterable<Occurrence> occurrencesHappeningOnce = filter(map, new IsRepeatingAtFrequencyFilter(FrequencyTypes.ONCE));
        assertThat(occurrencesHappeningOnce).hasSize(1);
        assertThat(occurrencesHappeningOnce.iterator().next()).isEqualTo(occurrence(timelineStart.withTime(23, 11, 0, 0), FrequencyTypes.ONCE));
    }

    private Occurrence occurrence(DateTime dateTime, FrequencyTypes frequency) {
        Occurrence occurrence = new Occurrence();
        occurrence.setStartDate(dateTime.toDate());
        occurrence.setEndDate(dateTime.plusHours(1).toDate());
        occurrence.setFrequencyType(new FrequencyType().setValue(frequency.name()));
        occurrence.setEvent(event);
        return occurrence;
    }

}
