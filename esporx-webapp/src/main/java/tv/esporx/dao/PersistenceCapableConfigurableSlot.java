package tv.esporx.dao;

import java.util.List;

import javax.persistence.EntityManager;

import tv.esporx.domain.front.ConfigurableSlot;

public interface PersistenceCapableConfigurableSlot {

	List<ConfigurableSlot> findAll();

	List<ConfigurableSlot> findByLanguage(String language);

	ConfigurableSlot findById(Long id);

	void saveOrUpdate(ConfigurableSlot slot);

	void setEntityManager(EntityManager entityManager);

	void delete(ConfigurableSlot slot);
}
