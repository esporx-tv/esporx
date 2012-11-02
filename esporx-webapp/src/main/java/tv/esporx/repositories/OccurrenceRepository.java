package tv.esporx.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tv.esporx.domain.Event;
import tv.esporx.domain.Occurrence;

import java.util.Collection;
import java.util.List;

public interface OccurrenceRepository extends CrudRepository<Occurrence,Long>, OccurrenceRepositoryCustom {
    @Query( "SELECT DISTINCT occ " +                //
            "FROM Occurrence occ " +                //
            "LEFT JOIN FETCH occ.channels cha " +   //
            "WHERE occ.event = ?1" )       //
    List<Occurrence> findByEvent(Event event);
}
