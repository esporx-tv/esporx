package tv.esporx.dao.impl;

import static org.fest.assertions.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableVideoProvider;
import tv.esporx.domain.VideoProvider;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
	locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
	"classpath:/META-INF/spring/testApplicationContext.xml"})
@Transactional
public class VideoProviderRepositoryIT {

	@Autowired
	private PersistenceCapableVideoProvider repository;
	@PersistenceContext
	private EntityManager entityManager;
	private VideoProvider provider;

	@Before
	public void setup() {
		givenDataHasBeenPurged();
		givenOneProviderHasBeenInserted();
	}

	@Test(expected = PersistenceException.class)
	public void when_persisting_same_pattern_then_exception() {
		VideoProvider copy = new VideoProvider();
		copy.setPattern("^(?:(?:https?):\\/\\/)?(?:www\\.)?regame.tv\\/(?:live|vod)\\/([0-9]+).*");
		copy.setTemplate("whatever");
		repository.saveOrUpdate(copy);
	}

	private void givenDataHasBeenPurged() {
		entityManager.createNativeQuery("delete from video_providers").executeUpdate();
	}

	private void givenOneProviderHasBeenInserted() {
		provider = new VideoProvider();
		provider.setPattern("^(?:(?:https?):\\/\\/)?(?:www\\.)?regame.tv\\/(?:live|vod)\\/([0-9]+).*");
		provider.setTemplate("<iframe src=\"http://www.regame.tv/playflash.php?vid={ID}&remote=true&w=630&h=400\" width=\"650\" height=\"420\" frameborder=0></iframe>");
		assertThat(repository).isNotNull();
		repository.saveOrUpdate(provider);
		assertThat(provider.getId()).isGreaterThan(0L);
	}
}
