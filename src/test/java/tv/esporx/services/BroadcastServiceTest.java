package tv.esporx.services;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import tv.esporx.domain.Event;
import tv.esporx.domain.FrequencyType;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.EventRepository;
import tv.esporx.services.timeline.Timeline;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static tv.esporx.domain.FrequencyType.FrequencyTypes.ONCE;

@RunWith(MockitoJUnitRunner.class)
public class BroadcastServiceTest {

    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private TimelineService timelineService;
    @InjectMocks
    private BroadcastService service = new BroadcastService();

    @Before
    public void prepare() {
        initMocks(this);
    }

    @Test
    public void should_sort_broadcasts_per_start_date() {
        givenTwoNextOccurrences();
        Collection<Event> occurrences = service.findUpNext(2);
        assertThat(occurrences).hasSize(2).onProperty("id").containsOnly(1L, 2L);
        assertThat(occurrences.iterator().next().getId()).isEqualTo(1L);
    }

    private void givenTwoNextOccurrences() {
        DateTime start = new DateTime();
        DateTime end = start.plusHours(8);
        Timeline timeline = new Timeline(start, end);
        timeline.from(newArrayList(
            occurrence(1L, start.plusHours(3)),
            occurrence(2L, start.plusHours(5))
        ));
        when(timelineService.getTimeline(any(DateTime.class), any(DateTime.class))).thenReturn(timeline);
    }

    private Occurrence occurrence(Long id, DateTime start) {
        Occurrence occurrence = new Occurrence();
        occurrence.setFrequencyType(new FrequencyType(ONCE));
        occurrence.setStartDate(start.toDate());
        Event event = new Event();
        event.setId(id);
        event.setTitle("Event #" + id);
        occurrence.setEvent(event);
        return occurrence;
    }
}
