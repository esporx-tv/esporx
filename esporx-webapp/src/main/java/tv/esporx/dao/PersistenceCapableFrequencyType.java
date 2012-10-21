package tv.esporx.dao;

import java.util.List;

import tv.esporx.domain.FrequencyType;

public interface PersistenceCapableFrequencyType {

    public List<FrequencyType> findAll();

    public FrequencyType findByValue(String value);
}
