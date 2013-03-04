package tv.esporx.controllers;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
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
import tv.esporx.domain.ConfigurableSlot;
import tv.esporx.framework.TestGenericWebXmlContextLoader;
import tv.esporx.repositories.ConfigurableSlotRepository;

import javax.sql.DataSource;

import static com.ninja_squad.dbsetup.Operations.*;
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
public class ConfigurableSlotControllerMappingIT {
	private MockMvc mvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private ConfigurableSlot slot;

	@Autowired
	private ConfigurableSlotRepository repository;

    @Autowired
    private DataSource dataSource;

    public static final long ID = 1L;
    private static final Operation DELETE_CONFIGURABLE_SLOTS =
            deleteAllFrom("configurable_slots");
    private static final Operation INSERT_CONFIGURABLE_SLOT =
            sequenceOf(
                    DELETE_CONFIGURABLE_SLOTS,
                    insertInto("configurable_slots")
                            .columns("id", "description", "link", "picture", "title", "position_x", "position_y", "width", "language", "is_active", "box_title")
                            .values(ID, "Super description", "http://www.link.com", "picture.gif", "Super Slot", 1L, 1L, 2, "en", true, "Super box title")
                            .build()
            );

	@Before
	public void setup() {
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), INSERT_CONFIGURABLE_SLOT);
        dbSetup.launch();
        slot = repository.findOne(ID);
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
		mvc.perform(get("/admin/slot/edit/" + (slot.getId() + 1000))).andExpect(status().isNotFound()).andExpect(view().name("channel/notFound"));
	}

}
