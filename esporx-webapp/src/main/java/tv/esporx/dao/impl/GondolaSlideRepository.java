package tv.esporx.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableGondolaSlide;
import tv.esporx.domain.front.GondolaSlide;

import com.google.common.base.Preconditions;

@Repository
public class GondolaSlideRepository implements PersistenceCapableGondolaSlide {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public GondolaSlide findById(final Long id) {
		Preconditions.checkArgument(id >= 0);
		return entityManager.find(GondolaSlide.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<GondolaSlide> findByLanguage(final String language) {
		TypedQuery<GondolaSlide> query = entityManager.createNamedQuery("GondolaSlide.findByLanguage", GondolaSlide.class);
		query.setParameter("language", language.toUpperCase());
		return query.getResultList();
	}

	@Override
	@Transactional
	public void saveOrUpdate(final GondolaSlide gondolaSlide) {
		entityManager.persist(gondolaSlide);
	}

	@Override
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional(readOnly = true)
	public List<GondolaSlide> findAll() {
		TypedQuery<GondolaSlide> query = entityManager.createNamedQuery("GondolaSlide.findAll", GondolaSlide.class);
		return query.getResultList();
	}

	@Override
	@Transactional
	public void delete(GondolaSlide slide) {
		entityManager.remove(slide);
	}

}
