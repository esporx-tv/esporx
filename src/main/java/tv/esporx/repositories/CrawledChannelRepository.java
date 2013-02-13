package tv.esporx.repositories;

import org.springframework.data.repository.CrudRepository;
import tv.esporx.domain.CrawledChannel;

/**
 * Basic Crud repository to use crawled channels
 */
public interface CrawledChannelRepository extends CrudRepository<CrawledChannel, Long>{
}
