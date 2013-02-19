package tv.esporx.services;

import com.google.common.base.Predicate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Game;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.OccurrenceRepository;
import tv.esporx.services.timeline.Timeline;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.find;
import static tv.esporx.framework.time.DateTimeUtils.toStartHour;

@Service
@Transactional
public class TimelineService {

    @Autowired
    private OccurrenceRepository occurrenceRepository;

    @Transactional(readOnly = true)
    public Timeline getTimeline(DateTime start, DateTime end) {
        checkSanity(start, end);
        return new Timeline(start, end).from(occurrenceRepository.findAllInRange(start, end));
    }

    @Transactional(readOnly = true)
    public Occurrence findCurrentBroadcastByChannel(final Channel channel) {
        DateTime start = toStartHour(new DateTime());
        Timeline timeline = this.getTimeline(start, start.plusHours(1));
        return find(timeline.perHourMultimap().values(), new Predicate<Occurrence>() {
            @Override
            public boolean apply(Occurrence input) {
                return input.getChannels().contains(channel);
            }
        });
    }

    private void checkSanity(DateTime startDay, DateTime endDay) {
        checkNotNull(startDay);
        checkNotNull(endDay);
        checkArgument(startDay.isBefore(endDay));
    }

}
