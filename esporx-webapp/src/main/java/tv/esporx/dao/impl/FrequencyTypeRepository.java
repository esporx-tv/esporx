package tv.esporx.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableFrequencyType;
import tv.esporx.domain.FrequencyType;

@Repository
public class FrequencyTypeRepository implements PersistenceCapableFrequencyType {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public List<FrequencyType> findAll() {
        return entityManager.createNamedQuery("FrequencyType.findAll", FrequencyType.class).getResultList();
    }

    @Override
    public FrequencyType findByValue(String value) {
        return entityManager.createNamedQuery("FrequencyType.findByValue", FrequencyType.class).setParameter("value", value).getSingleResult();
    }
}
