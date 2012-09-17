package tv.esporx.dao;

import javax.persistence.EntityManager;

import tv.esporx.domain.VideoProvider;

public interface PersistenceCapableVideoProvider {

	void setEntityManager(EntityManager entityManager);

	void delete(VideoProvider videoProvider);

	boolean isValid(String url);

	String getEmbeddedContents(String url);

    void saveOrUpdate(VideoProvider videoProvider);

    VideoProvider findByUrl(String url);

}
