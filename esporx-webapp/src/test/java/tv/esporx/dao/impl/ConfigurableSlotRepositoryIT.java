package tv.esporx.dao.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableConfigurableSlot;
import tv.esporx.domain.front.ConfigurableSlot;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
	locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
	"classpath:/META-INF/spring/testApplicationContext.xml"})
@Transactional
public class ConfigurableSlotRepositoryIT {

	@Autowired
	private PersistenceCapableConfigurableSlot configurableSlotRepository;

	@PersistenceContext
	private EntityManager entityManager;
	private ConfigurableSlot insertedSlot;

	@Before
	public void setup() {
		givenDataHasBeenPurged();
		givenOneSlotIsInserted();
	}

	@Test
	public void when_calling_findAll_then_retrieved_cast_is_the_same() {
		List<ConfigurableSlot> slots = configurableSlotRepository.findAll();
		assertThat(slots).containsOnly(insertedSlot);
	}

	@Test
	public void when_calling_findById_then_retrieved_cast_is_the_same() {
		ConfigurableSlot retrievedSlot = configurableSlotRepository.findById(insertedSlot.getId());
		assertThat(retrievedSlot).isEqualTo(insertedSlot);
	}

	@Test
	public void when_updating_slot_then_retrieved_cast_is_the_same() {
		long previousId = insertedSlot.getId();
		String newPicture = "updatePicture.png";
		insertedSlot.setPicture(newPicture);
		configurableSlotRepository.saveOrUpdate(insertedSlot);
		ConfigurableSlot retrievedSlot = configurableSlotRepository.findById(insertedSlot.getId());
		assertThat(retrievedSlot.getId()).isEqualTo(previousId);
		assertThat(retrievedSlot.getPicture()).isEqualTo(newPicture);
		assertThat(retrievedSlot).isEqualTo(insertedSlot);
	}

	private void givenDataHasBeenPurged() {
		entityManager.createNativeQuery("delete from configurable_slots").executeUpdate();
	}

	private void givenOneSlotIsInserted() {
		insertedSlot = new ConfigurableSlot();
		insertedSlot.setTitle("Slot");
		insertedSlot.setDescription("One superb slot");
		insertedSlot.setPicture("oneSlotToRuleThemAll.png");
		insertedSlot.setLink("http://www.yourslot.com");
		assertThat(configurableSlotRepository).isNotNull();
		configurableSlotRepository.saveOrUpdate(insertedSlot);
		assertThat(insertedSlot.getId()).isGreaterThan(0L);
	}

}
