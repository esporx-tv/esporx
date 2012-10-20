package tv.esporx.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tv.esporx.dao.PersistenceCapableOccurrence;
import tv.esporx.domain.Occurrence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class OccurrenceRepository implements PersistenceCapableOccurrence {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Long saveOrUpdate(Occurrence occurrence) {
        entityManager.persist(occurrence);
        return occurrence.getId();
    }

}
