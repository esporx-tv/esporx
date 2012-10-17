package tv.esporx.dao;

import tv.esporx.domain.FrequencyType;

import java.util.List;

public interface PersistenceCapableFrequencyType {

    public List<FrequencyType> findAll();
}
