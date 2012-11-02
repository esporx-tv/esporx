package tv.esporx.repositories;

import org.junit.Before;
import org.junit.Test;
import tv.esporx.domain.VideoProvider;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

public class CustomVideoProviderRepositoryTest {

	private VideoProviderRepositoryCustom repository;
	private EntityManager entityManager;
	private VideoProvider provider;
	private TypedQuery<VideoProvider> query;

	@Before
	public void setup() {
        givenDummyVideoProvider();
        givenPartiallyMockedCustomRepository();
	}

    @Test
	public void when_calling_isValid_then_getResultList_is_invoked() {
		repository.isValid("http://www.google.cn");
		verify(query).getResultList();
	}

	@Test
	public void when_calling_isValid_then_matching_result_is_returned() {
		assertThat(repository.isValid("http://www.youtube.com/watch?v=YwtjihXqCKA&feature=related")).isTrue();
		assertThat(repository.isValid("http://youtube.com/watch?v=YwtjihXqCKA&feature=related")).isTrue();
		assertThat(repository.isValid("http://www.youtube.com/watch?feature=related&v=YwtjihXqCKA")).isTrue();
		assertThat(repository.isValid("http://youtube.com/watch?feature=related&v=YwtjihXqCKA")).isTrue();
		assertThat(repository.isValid("https://www.youtube.com/watch?feature=related&v=YwtjihXqCKA")).isTrue();
		assertThat(repository.isValid("https://youtube.com/watch?v=YwtjihXqCKA&feature=related")).isTrue();
		assertThat(repository.isValid("www.youtube.com/watch?v=YwtjihXqCKA&feature=related")).isTrue();
		assertThat(repository.isValid("youtube.com/watch?v=YwtjihXqCKA&feature=related")).isTrue();
		assertThat(repository.isValid("youtube.com/watch?feature=related")).isFalse();
		assertThat(repository.isValid("www.google.cn")).isFalse();
	}

	@Test
	public void when_calling_getContents_then_matching_result_is_returned() {
		assertThat(repository.getEmbeddedContents("http://www.youtube.com/watch?v=YwtjihXqCKA&feature=related")).isEqualTo("<iframe width=\"425\" height=\"349\" src=\"https://www.youtube.com/embed/YwtjihXqCKA\" frameborder=\"0\" allowfullscreen></iframe>");
	}

    private void givenPartiallyMockedCustomRepository() {
        repository = new VideoProviderRepositoryImpl();
        entityManager = mock(EntityManager.class);
        query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(new ArrayList<VideoProvider>(asList(provider)));
        when(entityManager.createQuery(any(String.class), eq(VideoProvider.class))).thenReturn(query);
        setField(repository, "entityManager", entityManager);
    }

    private void givenDummyVideoProvider() {
        provider = new VideoProvider();
        provider.setPattern("^(?:(?:https?):\\/\\/)?(?:www\\.)?youtube.com\\/watch\\?(?:.*)v=([A-Za-z0-9._%-]{11}).*");
        provider.setTemplate("<iframe width=\"425\" height=\"349\" src=\"https://www.youtube.com/embed/{ID}\" frameborder=\"0\" allowfullscreen></iframe>");
    }
}
