package tv.esporx.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.OccurrenceRepository;

@Service
@Transactional
public class OccurrenceService {

	@Autowired
	private OccurrenceRepository occurrenceRepository;
    @Autowired
    private TimelineService timeline;

    @Transactional
	public Long saveWithAssociations(final Occurrence occurrence, final List<Long> channelIds) {
		occurrenceRepository.save(occurrence);
		occurrenceRepository.saveWithAssociations(occurrence, channelIds);
        timeline.getTimeline().addOrUpdate(occurrence);
		return occurrence.getId();
	}

    @Transactional
    public void delete(final Occurrence occurrence) {
        occurrenceRepository.delete(occurrence);
        timeline.getTimeline().delete(occurrence);
    }
}
