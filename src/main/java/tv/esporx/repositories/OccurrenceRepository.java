package tv.esporx.repositories;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tv.esporx.domain.Event;
import tv.esporx.domain.Occurrence;

import java.util.List;

public interface OccurrenceRepository extends CrudRepository<Occurrence,Long>, OccurrenceRepositoryCustom {
    @Query( "SELECT DISTINCT occ " +                                                //
            "FROM Occurrence occ " +                                                //
            "LEFT JOIN FETCH occ.channels " +                                       //
            "WHERE occ.event = ?1 " +
            "ORDER BY occ.startDate ASC" )
    List<Occurrence> findByEvent(Event event);

    @Query( "SELECT DISTINCT occ " +                                                //
            "FROM Occurrence occ " +                                                //
            "LEFT JOIN FETCH occ.channels " +                                       //
            "WHERE occ.startDate <= :end " +                                        //
            "AND (occ.endDate IS NULL OR occ.endDate > :start) " +
            "ORDER BY occ.startDate ASC" )
    List<Occurrence> findAllInRange(@Param("start") DateTime startDate, @Param("end") DateTime endDate);
}
