package tv.esporx.scheduling;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tv.esporx.domain.CrawledEvent;
import tv.esporx.repositories.CrawledEventRepository;

import javax.sql.DataSource;

import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Component
@Lazy(false)
public class CrawlerJob {
    private static final Logger LOGGER = getLogger(CrawlerJob.class);

    @Autowired
    CrawledEventRepository crawledEventRepository;

    public CrawlerJob() {
        LOGGER.info("Crawler started");
    }

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void runCrawledEventFetching() {
        LOGGER.info("Starting updates for crawled events from TeamLiquid...");
        //crawledEventRepository.deleteAllWithJPQL();
        LOGGER.debug("Truncated crawled events table successfully");
        List<CrawledEvent> events = fetchCrawledEvents();
        LOGGER.debug("Begin inserting events fetched");
        //crawledEventRepository.save(events);
    }

    private List<CrawledEvent> fetchCrawledEvents() {
        return Collections.emptyList();
    }
}
