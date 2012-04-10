package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.server.setup.MockMvcBuilders.webApplicationContextSetup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import tv.esporx.dao.PersistenceCapableConfigurableSlot;
import tv.esporx.domain.front.ConfigurableSlot;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml", "classpath:/META-INF/spring/testApplicationContext.xml" })
@Transactional
public class ConfigurableSlotControllerMappingIT {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	private ConfigurableSlot slot;
	@Autowired
	private PersistenceCapableConfigurableSlot slotDao;
	@PersistenceContext
	private EntityManager entityManager;

	@Before
	public void setup() {
		givenDataHasBeenPurged();
		givenOneSlotHasBeenInserted();
		mvc = webApplicationContextSetup(webApplicationContext).build();
	}

	@Test
	public void when_saving_invalid_slot_then_routed_to_form_page() throws Exception {
		mvc.perform(post("/admin/slot/new")).andExpect(status().isOk()).andExpect(view().name("configurableSlot/form"));
	}

	@Test
	public void when_accessing_slot_creation_page_then_routed_to_form_page() throws Exception {
		mvc.perform(get("/admin/slot/new")).andExpect(status().isOk()).andExpect(view().name("configurableSlot/form"));
	}

	@Test
	public void when_accessing_slot_edition_page_then_routed_to_form_page() throws Exception {
		mvc.perform(get("/admin/slot/edit/" + slot.getId())).andExpect(status().isOk()).andExpect(view().name("configurableSlot/form"));
	}

	@Test
	public void when_accessing_non_existing_slot_edition_page_then_routed_to_form_page() throws Exception {
		mvc.perform(get("/admin/slot/edit/" + (slot.getId() + 1000))).andExpect(status().isNotFound()).andExpect(view().name("configurableSlot/notFound"));
	}

	@Test
	public void when_accessing_slots_page_then_routed_to_list_page() throws Exception {
		mvc.perform(get("/admin/slot/browse")).andExpect(status().isOk()).andExpect(view().name("configurableSlot/list"));
	}

	private void givenDataHasBeenPurged() {
		entityManager.createNativeQuery("delete from configurable_slots").executeUpdate();
	}

	private void givenOneSlotHasBeenInserted() {
		slot = new ConfigurableSlot();
		slot.setTitle("Super slot");
		slot.setDescription("Super descriptionnnnnn");
		slot.setLink("http://www.link.com");
		slot.setPicture("myface.jpg");
		assertThat(slotDao).isNotNull();
		slotDao.saveOrUpdate(slot);
		assertThat(slot.getId()).isGreaterThan(0L);
	}
}
