package tv.esporx.repositories;

import java.util.List;

import tv.esporx.domain.Occurrence;

interface OccurrenceRepositoryCustom {
    /**
     * Occurrence persistence and batch insert of channel-occurrence associations
     * @param occurrence
     * @param channelIds
     */
    public Long saveWithAssociations(final Occurrence occurrence, final List<Long> channelIds);
}
