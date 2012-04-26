package tv.esporx.dao;

import java.util.List;

import javax.persistence.EntityManager;

import tv.esporx.domain.front.GondolaSlide;

public interface PersistenceCapableGondolaSlide {

	GondolaSlide findById(long id);

	List<GondolaSlide> findByLanguage(String language);

	void saveOrUpdate(GondolaSlide gondolaSlide);

	void setEntityManager(EntityManager entityManager);

	List<GondolaSlide> findAll();

	void delete(GondolaSlide slide);

}
