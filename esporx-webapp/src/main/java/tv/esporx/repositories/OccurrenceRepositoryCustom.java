package tv.esporx.repositories;

import tv.esporx.domain.Occurrence;

import java.util.List;

interface OccurrenceRepositoryCustom {
    /**
     * Occurrence persistence and batch insert of channel-occurrence associations
     * @param occurrence
     * @param channelIds
     */
    public Long saveWithAssociations(final Occurrence occurrence, final List<Long> channelIds);
}
