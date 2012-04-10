package tv.esporx.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableGame;
import tv.esporx.domain.Game;

@Repository
public class GameRepository implements PersistenceCapableGame {

	@PersistenceContext
	public EntityManager entityManager;

	@Override
	@Transactional(readOnly = true)
	public Game findById(final long id) {
		Game game = entityManager.find(Game.class, id);
		return game;
	}

	@Override
	@Transactional(readOnly = true)
	public Game findByTitle(final String title) {
		Query query = entityManager.createNamedQuery("Game.findByTitle");
		query.setParameter("title", title.toUpperCase()).setMaxResults(1);
		try {
			return (Game) query.getSingleResult();
		}
		catch (NoResultException nre) {
			return null;
		}
	}

	@Override
	@Transactional
	public void saveOrUpdate(final Game game) {
		entityManager.persist(game);
	}

	@Override
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
