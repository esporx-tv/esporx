package tv.esporx.dao;

import java.util.List;

import javax.persistence.EntityManager;

import tv.esporx.domain.front.ConfigurableSlot;

public interface PersistenceCapableConfigurableSlot {

	List<ConfigurableSlot> findAll();

	ConfigurableSlot findById(long id);

	void saveOrUpdate(ConfigurableSlot slot);

	void setEntityManager(EntityManager entityManager);

}
