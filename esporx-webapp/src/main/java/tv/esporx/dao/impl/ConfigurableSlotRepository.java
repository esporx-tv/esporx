package tv.esporx.dao.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableConfigurableSlot;
import tv.esporx.domain.front.ConfigurableSlot;

@Repository
public class ConfigurableSlotRepository implements PersistenceCapableConfigurableSlot {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ConfigurableSlot> findAll() {
		Query query = entityManager.createNamedQuery("ConfigurableSlot.findAll");
		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public ConfigurableSlot findById(final long id) {
		checkArgument(id >= 0);
		return entityManager.find(ConfigurableSlot.class, id);
	}

	@Override
	@Transactional
	public void saveOrUpdate(final ConfigurableSlot slot) {
		entityManager.persist(slot);
	}

	@Override
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional
	public void delete(ConfigurableSlot slot) {
		entityManager.remove(slot);
		
	}


}
