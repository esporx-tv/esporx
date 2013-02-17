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
import tv.esporx.domain.GondolaSlide;
import tv.esporx.framework.TestGenericWebXmlContextLoader;
import tv.esporx.repositories.GondolaSlideRepository;

import javax.sql.DataSource;
import java.sql.Date;

import static com.ninja_squad.dbsetup.Operations.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.webApplicationContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = { "classpath:esporx-servlet.xml", "classpath:applicationContext.xml", "classpath:/META-INF/spring/testApplicationContext.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class GondolaSlideControllerMappingIT {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private GondolaSlideRepository slideRepositoryDao;
	private GondolaSlide slide;
    @Autowired
    private DataSource dataSource;

    private static final Operation DELETE_GONDOLA_SLIDES =
            deleteAllFrom("gondola_slides");
    public static final long ID = 1L;
    private static final Operation INSERT_GONDOLA_SLIDE =
            sequenceOf(
                DELETE_GONDOLA_SLIDES,
                insertInto("gondola_slides")
                        .columns("id", "date", "description", "language", "link", "picture", "prize", "tag_line", "title")
                        .values(ID, new Date(new DateTime().getMillis()), "blablablabla", "fr", "esporx.kr", "inyourface.gif", "$10", "Incredible", "Oh yeah title")
                        .build()
            );

	@Before
	public void setup() {
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), INSERT_GONDOLA_SLIDE);
        dbSetup.launch();
        slide = slideRepositoryDao.findOne(ID);
        mvc = webApplicationContextSetup(webApplicationContext).build();
	}

	@Test
	public void when_requesting_gondola_slide_creation_page_then_routed_to_form() throws Exception {
		mvc.perform(get("/admin/slide/new"))
		.andExpect(status().isOk())
		.andExpect(view().name("slide/form"))
		.andExpect(model().attribute("gondolaSlideCommand", is(GondolaSlide.class)));
	}

	@Test
	public void when_saving_invalid_gondola_slide_then_routed_to_form() throws Exception {
		mvc.perform(post("/admin/slide/new")).andExpect(status().isOk()).andExpect(view().name("slide/form"));
	}

	@Test
	public void when_saving_valid_gondola_slide_then_routed_to_home() throws Exception {
		mvc.perform(post("/admin/slide/new").param("date", "28/03/2015 12:13").param("description", "blablabla").param("tagLine", "zuper tagline").param("language", "de").param("prize", "1 zillion €").param("title", "zuper title").param("link", "http://www.google.cn").param("picture", "yourface.gif")).andExpect(status().isOk()).andExpect(view().name("redirect:/admin/home?active=gondola"));
	}

	@Test
	public void when_requesting_invalid_gondola_slide_edition_page_then_routed_to_not_found() throws Exception {
		mvc.perform(get("/admin/slide/edit/-4")).andExpect(status().isNotFound()).andExpect(view().name("slide/notFound"));
	}

	@Test
	public void when_requesting_valid_gondola_slide_edition_page_then_routed_to_edition_page() throws Exception {
		mvc.perform(get("/admin/slide/edit/" + slide.getId())).andExpect(status().isOk()).andExpect(view().name("slide/form"));
	}

	@Test
	public void when_requesting_non_existing_gondola_slide_edition_page_then_routed_to_not_found() throws Exception {
		mvc.perform(get("/admin/slide/edit/" + (slide.getId() + 1000L))).andExpect(status().isNotFound()).andExpect(view().name("slide/notFound"));
	}

}
