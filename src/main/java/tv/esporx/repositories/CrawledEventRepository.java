package tv.esporx.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tv.esporx.domain.CrawledEvent;

/**
 * Basic crud repository for CrawledEvents
 */
public interface CrawledEventRepository extends CrudRepository<CrawledEvent, Long> {
    @Query(value = "DELETE FROM CrawledEvent")
    void deleteAllWithJPQL();
}
