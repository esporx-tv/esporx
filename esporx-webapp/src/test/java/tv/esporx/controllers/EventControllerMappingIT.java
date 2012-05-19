package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.server.setup.MockMvcBuilders.webApplicationContextSetup;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.domain.Event;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml", "classpath:/META-INF/spring/testApplicationContext.xml" })
@Transactional
public class EventControllerMappingIT {
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mvc;
	private Event event;
	@Autowired
	private PersistenceCapableEvent eventRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Before
	public void setup() {
		givenDataHasBeenPurged();
		givenOneEventIsInserted();
		mvc = webApplicationContextSetup(webApplicationContext).build();
	}

	@Test
	public void when_accessing_event_then_routed_to_index_page() throws Exception {
		mvc.perform(get("/event/see/" + event.getId())).andExpect(status().isOk()).andExpect(view().name("event/index"));
	}

	@Test
	public void when_accessing_event_with_invalid_id_then_routed_to_not_found() throws Exception {
		mvc.perform(get("/event/see/-25")).andExpect(status().isNotFound()).andExpect(view().name("cast/notFound"));
	}

	@Test
	public void when_event_creation_page_then_routed_to_form() throws Exception {
		mvc.perform(get("/event/new")).andExpect(status().isOk()).andExpect(view().name("event/form"));
	}

	@Test
	public void when_accessing_event_edition_then_routed_to_edition_page() throws Exception {
		mvc.perform(get("/event/edit/" + event.getId())).andExpect(status().isOk()).andExpect(view().name("event/form"));
	}

	@Test
	public void when_accessing_event_edition_with_invalid_id_then_routed_to_not_found() throws Exception {
		mvc.perform(get("/event/edit/-65")).andExpect(status().isNotFound()).andExpect(view().name("cast/notFound"));
	}

	@Test
	public void when_saving_valid_event_then_routed_to_admin_home() throws Exception {
		mvc.perform(post("/event/new").param("title", "Event Title").param("description", "Event description").param("startDate", "28/03/2015 12:13").param("endDate", "12/12/2018 12:12")).andExpect(status().isOk()).andExpect(view().name("redirect:/admin/home"));
	}

	@Test
	public void when_saving_invalid_event_then_routed_to_form() throws Exception {
		mvc.perform(post("/event/new").param("description", "Event description").param("startDate", "28/03/2015 12:13").param("endDate", "12/12/2018 12:12")).andExpect(status().isOk()).andExpect(view().name("event/form"));
	}

	@Test
	@Ignore
	public void when_deleting_event_with_invalid_id_then_routed_to_not_found() throws Exception {
		mvc.perform(post("/event/remove/-69")).andExpect(status().isNotFound()).andExpect(view().name("cast/notFound"));
	}

	@Test
	@Ignore
	public void when_deleting_event_then_routed_to_home() throws Exception {
		mvc.perform(post("/event/remove/" + event.getId())).andExpect(status().isOk()).andExpect(view().name("redirect:/admin/home"));
	}

	private void givenDataHasBeenPurged() {
		entityManager.createNativeQuery("delete from events").executeUpdate();
	}

	private void givenOneEventIsInserted() {
		event = new Event();
		event.setTitle("Zuper event");
		event.setDescription("Awesome description");
		event.setStartDate(new Date(100000L));
		event.setEndDate(new Date());
		assertThat(eventRepository).isNotNull();
		eventRepository.saveOrUpdate(event);
		assertThat(event.getId()).isGreaterThan(0L);
	}

}
