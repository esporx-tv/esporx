package tv.esporx.dao;

import java.util.List;

import javax.persistence.EntityManager;

import tv.esporx.domain.Cast;

public interface PersistenceCapableCast {

	void delete(Cast cast);

	Cast findById(long i);

	Cast findByTitle(String string);

	List<Cast> findMostViewed();

	void saveOrUpdate(Cast cast);

	void setEntityManager(EntityManager entityManager);

	List<Cast> findAll();

}
