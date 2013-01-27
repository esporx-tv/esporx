package tv.esporx.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.OccurrenceRepository;

import java.util.List;

@Service
@Transactional
public class OccurrenceService {

	@Autowired
	private OccurrenceRepository occurrenceRepository;

    @Transactional
	public Long saveWithAssociations(final Occurrence occurrence, final List<Long> channelIds) {
		occurrenceRepository.save(occurrence);
		occurrenceRepository.saveWithAssociations(occurrence, channelIds);
		return occurrence.getId();
	}

    @Transactional
    public void delete(final Occurrence occurrence) {
        occurrenceRepository.delete(occurrence);
    }
}
