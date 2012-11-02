package tv.esporx.repositories;

import org.springframework.transaction.annotation.Transactional;
import tv.esporx.domain.VideoProvider;

interface VideoProviderRepositoryCustom {

    /**
     * States if the given video URL is currently supported by one of the video providers
     * @param url
     * @return
     */
    boolean isValid(String url);

    /**
     * Retrieves the embeddable contents of the video from first the matching video provider
     * @param url
     * @return
     */
    String getEmbeddedContents(String url);

    @Transactional(readOnly = true)
    VideoProvider findByUrl(String url);
}
