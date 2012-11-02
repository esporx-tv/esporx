package tv.esporx.repositories;

import org.springframework.data.repository.CrudRepository;
import tv.esporx.domain.ConfigurableSlot;

import java.util.List;

public interface ConfigurableSlotRepository extends CrudRepository<ConfigurableSlot, Long> {
	List<ConfigurableSlot> findByLanguage(String language);
}
