package tv.esporx.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import tv.esporx.domain.Event;

public interface EventRepository extends CrudRepository<Event,Long> {
    @Query("FROM Event")
	List<Event> findUpNext();
    
}
