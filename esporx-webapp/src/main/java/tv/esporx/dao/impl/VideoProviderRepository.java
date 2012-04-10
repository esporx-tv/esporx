package tv.esporx.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableVideoProvider;
import tv.esporx.domain.VideoProvider;

@Repository
public class VideoProviderRepository implements PersistenceCapableVideoProvider {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional
	public void delete(final VideoProvider videoProvider) {
		entityManager.remove(videoProvider);
	}

	@Override
	@Transactional(readOnly = true)
	public String getEmbeddedContents(final String url) {
		TypedQuery<VideoProvider> query = entityManager.createNamedQuery("VideoProvider.findAll", VideoProvider.class);
		for (VideoProvider provider : query.getResultList()) {
			try {
				return provider.getContents(url);
			}
			catch (IllegalArgumentException iae) {
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isValid(final String url) {
		return getEmbeddedContents(url) != null;
	}

	@Override
	@Transactional
	public void saveOrUpdate(final VideoProvider videoProvider) {
		entityManager.persist(videoProvider);
	}
}
