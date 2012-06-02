package tv.esporx.dao.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableGondolaSlide;
import tv.esporx.domain.front.GondolaSlide;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
"classpath:/META-INF/spring/testApplicationContext.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class GondolaSlideRepositoryIT {

	private GondolaSlide gondolaSlide;
	@Autowired
	private PersistenceCapableGondolaSlide gondolaSlideRepository;

	@Before
	public void setup() {
		givenOneInsertedSlide();
	}

	@Test
	public void when_calling_findById_then_retrieved_slide_is_the_same() {
		GondolaSlide retrievedSlide = gondolaSlideRepository.findById(gondolaSlide.getId());
		assertThat(retrievedSlide).isEqualTo(gondolaSlide);
	}

	@Test
	public void when_calling_findByLanguage_then_retrieved_slide_is_the_same() {
		List<GondolaSlide> retrievedSlides = gondolaSlideRepository.findByLanguage("fr_fr");
		assertThat(retrievedSlides).containsOnly(gondolaSlide);
	}

	@Test
	public void when_updating_slide_then_retrieved_slide_has_changes() {
		long previousId = gondolaSlide.getId();
		String newLanguage = "en_US";
		gondolaSlide.setLanguage(newLanguage);
		gondolaSlideRepository.saveOrUpdate(gondolaSlide);
		GondolaSlide retrievedSlide = gondolaSlideRepository.findById(gondolaSlide.getId());
		assertThat(retrievedSlide.getId()).isEqualTo(previousId);
		assertThat(retrievedSlide.getLanguage()).isEqualTo(newLanguage);
		assertThat(retrievedSlide).isEqualTo(gondolaSlide);
		assertThat(gondolaSlideRepository.findByLanguage("fr_FR")).isEmpty();
	}

	private void givenOneInsertedSlide() {
		gondolaSlide = new GondolaSlide();
		gondolaSlide.setTitle("TeH SLIDE");
		gondolaSlide.setDescription("A useful description");
		gondolaSlide.setPicture("myface.png");
		gondolaSlide.setTagLine("Read that, f*ckerz!");
		gondolaSlide.setLanguage("fr_FR");
		gondolaSlide.setPrize("2000€");
		gondolaSlide.setLink("http://thats.a.link.com");
		gondolaSlide.setDate(new Date());
		assertThat(gondolaSlideRepository).isNotNull();
		gondolaSlideRepository.saveOrUpdate(gondolaSlide);
		assertThat(gondolaSlide.getId()).isGreaterThan(0L);
	}
}
