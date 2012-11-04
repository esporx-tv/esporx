package tv.esporx.repositories;

import org.springframework.transaction.annotation.Transactional;
import tv.esporx.domain.VideoProvider;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * This a custom hook to built-in JPA methods provided by SpringData.
 * This becomes automagically managed by Spring container, no need to expose it directly in the context.
 * DO NOT USE THIS DIRECTLY
 */
class VideoProviderRepositoryImpl implements VideoProviderRepositoryCustom {

    public static final String FIND_ALL = "FROM VideoProvider ORDER BY id ASC";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(final String url) {
        return getEmbeddedContents(url) != null;
    }

    @Transactional(readOnly = true)
    public String getEmbeddedContents(final String url) {
        for (VideoProvider provider : allProviders()) {
            String videoEmbeddedContents = provider.getContents(url);
            if(videoEmbeddedContents != null) {
                return videoEmbeddedContents;
            }
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public VideoProvider findByUrl(String url) {
        for (VideoProvider provider : allProviders()) {
            String videoEmbeddedContents = provider.getContents(url);
            if(videoEmbeddedContents != null) {
                return provider;
            }
        }
        return null;
    }

    private List<VideoProvider> allProviders() {
        return entityManager.createQuery(FIND_ALL, VideoProvider.class).getResultList();
    }
}
