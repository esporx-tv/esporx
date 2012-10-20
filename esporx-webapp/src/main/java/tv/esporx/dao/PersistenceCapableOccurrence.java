package tv.esporx.dao;

import tv.esporx.domain.Occurrence;

public interface PersistenceCapableOccurrence {
    Long saveOrUpdate(Occurrence occurrence);
}
