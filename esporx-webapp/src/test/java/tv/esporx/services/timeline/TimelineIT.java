package tv.esporx.services.timeline;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import tv.esporx.collections.RepeatingOccurrencePredicate;
import tv.esporx.domain.Event;
import tv.esporx.domain.FrequencyType;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.EventRepository;
import tv.esporx.repositories.OccurrenceRepository;

import static com.google.common.collect.Iterables.filter;
import static org.fest.assertions.Assertions.assertThat;
import static tv.esporx.domain.FrequencyType.FrequencyTypes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration ({ "classpath*:applicationContext.xml",
    "classpath:META-INF/spring/testApplicationContext.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
@Ignore("WIP")
public class TimelineIT {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private OccurrenceRepository occurrenceRepository;
    @Autowired
    private Timeline timeline;
    private DateTime timelineStart;
    private Event event;

    @Before
    public void setUp() {
        timelineStart = new DateTime().withTime(22, 0, 0, 0);
        eventRepository.save(event());
        occurrenceRepository.save(occurrence(timelineStart.withTime(22, 11, 0, 0), FrequencyTypes.ONCE));
        occurrenceRepository.save(occurrence(timelineStart.withTime(22, 12, 0, 0), FrequencyTypes.DAILY));
        occurrenceRepository.save(occurrence(timelineStart.withTime(22, 13, 0, 0), FrequencyTypes.WEEKLY));
        occurrenceRepository.save(occurrence(timelineStart.withTime(22, 14, 0, 0), FrequencyTypes.MONTHLY));
        occurrenceRepository.save(occurrence(timelineStart.withTime(22, 15, 0, 0), FrequencyTypes.YEARLY));
    }

    @Test
    public void should_be_only_occurrence_happening_once() {
        timeline.update(timelineStart, timelineStart.plusYears(2));
        Iterable<Occurrence> occurrencesHappeningOnce = filter(timeline.getAll(), new RepeatingOccurrencePredicate(FrequencyTypes.ONCE));
        assertThat(occurrencesHappeningOnce).hasSize(1);
        assertThat(occurrencesHappeningOnce.iterator().next()).isEqualTo(occurrence(timelineStart.withTime(22, 11, 0, 0), FrequencyTypes.ONCE));
    }

    private Event event() {
        event = new Event();
        event.setTitle("Whatever, man");
        event.setDescription("So loooooooooooooong");
        event.setHighlighted(false);
        return event;
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
