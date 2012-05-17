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
import org.springframework.test.context.transaction.TransactionConfiguration;
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
@TransactionConfiguration(defaultRollback = true)
public class ConfigurableSlotRepositoryIT {

	@Autowired
	private PersistenceCapableConfigurableSlot configurableSlotRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private ConfigurableSlot insertedSlot;
	private ConfigurableSlot equivalentSlot;

	@Before
	public void setup() {
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

	@Test
	public void when_setting_one_slot_as_active_then_all_others_at_same_language_and_position_are_set_as_inactive() {
		givenASimilarSlotIsInserted();
		insertedSlot.setActive(true);
		configurableSlotRepository.saveOrUpdate(insertedSlot);
		entityManager.refresh(insertedSlot);
		entityManager.refresh(equivalentSlot);
		assertThat(configurableSlotRepository.findById(insertedSlot.getId()).isActive()).isTrue();
		assertThat(configurableSlotRepository.findById(equivalentSlot.getId()).isActive()).isFalse();
	}


	private void givenOneSlotIsInserted() {
		insertedSlot = new ConfigurableSlot();
		insertedSlot.setTitle("Slot");
		insertedSlot.setDescription("One superb slot");
		insertedSlot.setPicture("oneSlotToRuleThemAll.png");
		insertedSlot.setLink("http://www.yourslot.com");
		insertedSlot.setLanguage("en");
		insertedSlot.setActive(false);
		insertedSlot.setPosition(1);
		assertThat(configurableSlotRepository).isNotNull();
		configurableSlotRepository.saveOrUpdate(insertedSlot);
		assertThat(insertedSlot.getId()).isGreaterThan(0L);
	}

	private void givenASimilarSlotIsInserted() {
		equivalentSlot = new ConfigurableSlot();
		equivalentSlot.setTitle("Slot Bis");
		equivalentSlot.setDescription("Another superb slot");
		equivalentSlot.setPicture("twoSlotsToRuleThemAll.png");
		equivalentSlot.setLink("http://www.yoursecondslot.com");
		equivalentSlot.setLanguage("en");
		equivalentSlot.setActive(true);
		equivalentSlot.setPosition(1);
		assertThat(configurableSlotRepository).isNotNull();
		configurableSlotRepository.saveOrUpdate(equivalentSlot);
		assertThat(equivalentSlot.getId()).isGreaterThan(0L);
	}

}
