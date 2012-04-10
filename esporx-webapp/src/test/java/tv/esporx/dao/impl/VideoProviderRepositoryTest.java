package tv.esporx.dao.impl;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

import tv.esporx.dao.PersistenceCapableVideoProvider;
import tv.esporx.domain.VideoProvider;

public class VideoProviderRepositoryTest {

	private PersistenceCapableVideoProvider repository;
	private EntityManager entityManager;
	private VideoProvider provider;
	private TypedQuery<VideoProvider> query;

	@Before
	@SuppressWarnings("unchecked")
	public void setup() {
		repository = new VideoProviderRepository();
		entityManager = mock(EntityManager.class);
		query = mock(TypedQuery.class);
		provider = new VideoProvider();
		provider.setPattern("^(?:(?:https?):\\/\\/)?(?:www\\.)?youtube.com\\/watch\\?(?:.*)v=([A-Za-z0-9._%-]{11}).*");
		provider.setTemplate("<iframe width=\"425\" height=\"349\" src=\"https://www.youtube.com/embed/{ID}\" frameborder=\"0\" allowfullscreen></iframe>");
		when(query.getResultList()).thenReturn(new ArrayList<VideoProvider>(asList(provider)));
		when(entityManager.createNamedQuery("VideoProvider.findAll", VideoProvider.class)).thenReturn(query);
		repository.setEntityManager(entityManager);
	}

	@Test
	public void when_calling_save_then_entity_manager_persist_is_called() {
		repository.saveOrUpdate(provider);
		verify(entityManager).persist(provider);
	}

	@Test
	public void when_calling_delete_then_entity_manager_remove_is_called() {
		repository.delete(provider);
		verify(entityManager).remove(provider);
	}

	@Test
	public void when_calling_isValid_then_getResultList_is_invoked() {
		repository.isValid("http://www.google.cn");
		verify(entityManager).createNamedQuery("VideoProvider.findAll", VideoProvider.class);
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
}
