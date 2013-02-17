package tv.esporx.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tv.esporx.domain.Event;

import java.util.List;

public interface EventRepository extends CrudRepository<Event,Long> {
    @Query("FROM Event")
	List<Event> findUpNext();
    
}
