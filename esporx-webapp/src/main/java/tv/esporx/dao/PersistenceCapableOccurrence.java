package tv.esporx.dao;

import tv.esporx.domain.Occurrence;

import java.util.List;

public interface PersistenceCapableOccurrence {
    Occurrence findById(Long id);
    Long saveOrUpdate(Occurrence occurrence, List<Long> channelIds, Long eventId);
}
