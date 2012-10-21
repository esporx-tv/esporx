package tv.esporx.dao;

import java.util.Collection;
import java.util.List;

import tv.esporx.domain.Occurrence;

public interface PersistenceCapableOccurrence {
    Occurrence findById(Long id);
    Collection<Occurrence> findByEventId(Long eventId);
    Long saveOrUpdate(Occurrence occurrence, List<Long> channelIds);
	void delete(Occurrence occurrence);
}
