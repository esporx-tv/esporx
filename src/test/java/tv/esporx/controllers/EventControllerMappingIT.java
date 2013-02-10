package tv.esporx.controllers;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.joda.time.DateTime;
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
import org.springframework.test.web.server.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tv.esporx.domain.Event;
import tv.esporx.framework.TestGenericWebXmlContextLoader;
import tv.esporx.repositories.EventRepository;

import javax.sql.DataSource;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.server.setup.MockMvcBuilders.webApplicationContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = { "classpath:esporx-servlet.xml", "classpath:applicationContext.xml", "classpath:/META-INF/spring/testApplicationContext.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class EventControllerMappingIT {
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mvc;
	private Event event;
	@Autowired
	private EventRepository eventRepository;
    @Autowired
    private DataSource dataSource;

    private static final long ID = 1L;
    private static final Operation DELETE_EVENTS =
            deleteAllFrom("events");
    private static final Operation INSERT_EVENT =
            sequenceOf(
                DELETE_EVENTS,
                insertInto("events")
                        .columns("id", "description", "title", "highlight")
                        .values(ID, "blablablabla", "Oh yeah title", false)
                     .build()
            );

	@Before
	public void setup() {
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), INSERT_EVENT);
        dbSetup.launch();
        event = eventRepository.findOne(ID);
		mvc = webApplicationContextSetup(webApplicationContext).build();
	}

	@Test
	public void when_accessing_event_then_routed_to_index_page() throws Exception {
		mvc.perform(get("/event/see/" + event.getId())).andExpect(status().isOk()).andExpect(view().name("event/index"));
	}

	@Test
	public void when_accessing_event_with_invalid_id_then_routed_to_not_found() throws Exception {
		mvc.perform(get("/event/see/-25")).andExpect(status().isNotFound()).andExpect(view().name("channel/notFound"));
	}

	@Test
	public void when_event_creation_page_then_routed_to_form() throws Exception {
		mvc.perform(get("/admin/event/new")).andExpect(status().isOk()).andExpect(view().name("event/form"));
	}

	@Test
	public void when_accessing_event_edition_then_routed_to_edition_page() throws Exception {
		mvc.perform(get("/admin/event/edit/" + event.getId())).andExpect(status().isOk()).andExpect(view().name("event/form"));
	}

	@Test
	public void when_accessing_event_edition_with_invalid_id_then_routed_to_not_found() throws Exception {
		mvc.perform(get("/admin/event/edit/-65")).andExpect(status().isNotFound()).andExpect(view().name("channel/notFound"));
	}

	@Test
	public void when_saving_valid_event_then_routed_to_admin_home() throws Exception {
		mvc.perform(post("/admin/event/new").param("title", "Event Title").param("description", "Event description").param("startDate", "28/03/2015 12:13").param("endDate", "12/12/2018 12:12").param("highlighted", "0")).andExpect(status().isOk()).andExpect(view().name("redirect:/admin/home?active=event"));
	}

	@Test
	public void when_saving_invalid_event_then_routed_to_form() throws Exception {
		mvc.perform(post("/admin/event/new").param("description", "Event description").param("startDate", "28/03/2015 12:13").param("endDate", "12/12/2018 12:12")).andExpect(status().isOk()).andExpect(view().name("event/form"));
	}

	@Test
	public void when_deleting_event_with_invalid_id_then_routed_to_not_found() throws Exception {
		mvc.perform(post("/admin/event/remove").param("id", "-10000")).andExpect(status().isNotFound()).andExpect(view().name("channel/notFound"));
	}

	@Test
	public void when_deleting_event_then_routed_to_home() throws Exception {
		mvc.perform(post("/admin/event/remove").param("id", "" + event.getId())).andExpect(status().isOk()).andExpect(view().name("redirect:/admin/home?active=event"));
	}
}
