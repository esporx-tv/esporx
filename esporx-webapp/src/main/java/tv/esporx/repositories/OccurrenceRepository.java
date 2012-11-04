package tv.esporx.repositories;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tv.esporx.domain.Event;
import tv.esporx.domain.Occurrence;

import java.util.List;

public interface OccurrenceRepository extends CrudRepository<Occurrence,Long>, OccurrenceRepositoryCustom {
    @Query( "SELECT DISTINCT occ " +                                                //
            "FROM Occurrence occ " +                                                //
            "LEFT JOIN FETCH occ.channels " +                                       //
            "WHERE occ.event = ?1" )
    List<Occurrence> findByEvent(Event event);

    @Query( "SELECT DISTINCT occ " +                                                //
            "FROM Occurrence occ " +                                                //
            "LEFT JOIN FETCH occ.channels " +                                       //
            "WHERE (occ.startDate >= ?1 OR occ.frequencyType.value != 'ONCE')" +    //
            "AND (occ.endDate IS NULL OR occ.endDate <= ?2)" )
    List<Occurrence> findAllInRange(DateTime startDate, DateTime endDate);
}
