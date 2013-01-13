package tv.esporx.services;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tv.esporx.repositories.OccurrenceRepository;
import tv.esporx.services.timeline.Timeline;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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

    private void checkSanity(DateTime startDay, DateTime endDay) {
        checkNotNull(startDay);
        checkNotNull(endDay);
        checkArgument(startDay.isBefore(endDay));
    }

}
