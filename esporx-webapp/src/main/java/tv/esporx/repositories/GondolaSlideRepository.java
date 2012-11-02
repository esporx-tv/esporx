package tv.esporx.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import tv.esporx.domain.GondolaSlide;

import java.util.List;

public interface GondolaSlideRepository extends CrudRepository<GondolaSlide, Long> {
	List<GondolaSlide> findByLanguage(String language);
    List<GondolaSlide> findByLanguage(String language, Pageable sort);
}
