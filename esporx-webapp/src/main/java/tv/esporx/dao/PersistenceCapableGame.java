package tv.esporx.dao;

import javax.persistence.EntityManager;

import tv.esporx.domain.Game;

public interface PersistenceCapableGame {

	Game findById(Long id);

	Game findByTitle(String title);

	void saveOrUpdate(Game game);

	void setEntityManager(EntityManager entityManager);
}
