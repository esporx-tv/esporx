package tv.esporx.dao.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableConfigurableSlot;
import tv.esporx.domain.front.ConfigurableSlot;

@Repository
public class ConfigurableSlotRepository implements PersistenceCapableConfigurableSlot {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = true)
	public List<ConfigurableSlot> findAll() {
		TypedQuery<ConfigurableSlot> query = entityManager.createNamedQuery("ConfigurableSlot.findAll", ConfigurableSlot.class);
		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ConfigurableSlot> findByLanguage(final String language) {
		TypedQuery<ConfigurableSlot> query = entityManager.createNamedQuery("ConfigurableSlot.findByLanguage", ConfigurableSlot.class);
		query.setParameter("language", language);
		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public ConfigurableSlot findById(final Long id) {
		checkArgument(id >= 0);
		return entityManager.find(ConfigurableSlot.class, id);
	}

	@Override
	@Transactional
	public void saveOrUpdate(final ConfigurableSlot slot) {
		maybeUpdateOthersAsInactive(slot);
		entityManager.persist(slot);
	}

	private void maybeUpdateOthersAsInactive(final ConfigurableSlot slot) {
		if (slot.isActive()) {
			Query query = entityManager.createNamedQuery("ConfigurableSlot.setOthersInactive");
			query.setParameter("id", slot.getId());
			query.setParameter("position", slot.getPosition());
			query.setParameter("language", slot.getLanguage());
			query.executeUpdate();
		}
	}

	@Override
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional
	public void delete(final ConfigurableSlot slot) {
		entityManager.remove(slot);
	}


}
