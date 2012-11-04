package tv.esporx.services;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tv.esporx.services.timeline.Timeline;

import javax.annotation.PostConstruct;

import static tv.esporx.services.timeline.Timeline.MAX_MONTHS_AROUND_TODAY;

@Component
@Lazy(false)
public class TimelineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimelineService.class);
    private final Timeline timeline;

    @Autowired
    public TimelineService(Timeline timeline) {
        this.timeline = timeline;
    }

    @PostConstruct
    public void buildCache() {
        DateTime midnightToday = new DateTime().withTimeAtStartOfDay();
        DateTime start = midnightToday.minusMonths(MAX_MONTHS_AROUND_TODAY).plusDays(1);
        DateTime end = midnightToday.plusMonths(MAX_MONTHS_AROUND_TODAY).minusMillis(1);
        LOGGER.info("Building timeline cache between ["+ start +"] and [" + end + "].");
        this.timeline.update(start, end);
    }

    public Timeline getTimeline() {
        return timeline;
    }
}
