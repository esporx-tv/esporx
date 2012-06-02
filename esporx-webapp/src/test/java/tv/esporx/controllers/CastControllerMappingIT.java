package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.server.setup.MockMvcBuilders.webApplicationContextSetup;

import java.util.Date;

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

import tv.esporx.dao.PersistenceCapableCast;
import tv.esporx.dao.PersistenceCapableGame;
import tv.esporx.domain.Cast;
import tv.esporx.domain.Game;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml", "classpath:/META-INF/spring/testApplicationContext.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class CastControllerMappingIT {

	@Autowired
	private PersistenceCapableCast castRepository;
	@Autowired
	private PersistenceCapableGame gameRepository;
	@Autowired
	private WebApplicationContext webApplicationContext;

	private Cast cast;
	private Game game;
	private MockMvc mvc;

	@Before
	public void setup() {
		givenAtLeastOneGameIsStored();
		givenOneCastIsInserted();
		mvc = webApplicationContextSetup(webApplicationContext).build();
	}

	@Test
	public void when_requesting_cast_creation_page_then_routed_to_form() throws Exception {
		mvc.perform(get("/cast/new")).andExpect(status().isOk()).andExpect(view().name("cast/form"));
	}

	@Test
	public void when_requesting_cast_details_page_then_routed_to_form() throws Exception {
		mvc.perform(get("/cast/watch/" + cast.getId())).andExpect(status().isOk()).andExpect(view().name("cast/index"));
	}

	@Test
	public void when_requesting_cast_details_page_with_wrong_id_then_error() throws Exception {
		mvc.perform(get("/cast/watch/-10")).andExpect(status().isNotFound()).andExpect(view().name("cast/notFound"));
	}

	@Test
	public void when_requesting_cast_edition_page_then_routed_to_form() throws Exception {
		mvc.perform(get("/cast/edit/" + cast.getId())).andExpect(status().isOk()).andExpect(view().name("cast/form"));
	}

	@Test
	public void when_requesting_cast_edition_page_with_wrong_id_then_error() throws Exception{
		mvc.perform(get("/cast/edit/-10")).andExpect(status().isNotFound()).andExpect(view().name("cast/notFound"));
	}

	@Test
	public void when_requesting_non_existing_cast_details_page_then_routed_to_not_found() throws Exception {
		mvc.perform(get("/cast/watch/" + (cast.getId() + 1000))).andExpect(status().isNotFound()).andExpect(view().name("cast/notFound"));
	}

	@Test
	public void when_requesting_non_existing_cast_edition_page_with_wrong_id_then_error() throws Exception {
		mvc.perform(get("/cast/edit/" + (cast.getId() + 1000))).andExpect(status().isNotFound()).andExpect(view().name("cast/notFound"));
	}

	@Test
	public void when_requesting_non_existing_cast_removal_then_error() throws Exception {
		mvc.perform(post("/cast/remove").param("id", "100000")).andExpect(status().isNotFound()).andExpect(view().name("cast/notFound"));
	}

	@Test
	public void when_saving_invalid_cast_then_routed_to_form() throws Exception {
		mvc.perform(post("/cast/new").sessionAttr("currentGame", game.getTitle())).andExpect(status().isOk()).andExpect(view().name("cast/form"));
	}

	@Test
	public void when_saving_valid_cast_then_routed_to_home() throws Exception {
		mvc.perform(post("/cast/new").sessionAttr("currentGame", game.getTitle()).param("title", "Cast Title").param("language", "fr").param("videoUrl", "http://www.site.com").param("broadcastDate", "28/03/2015 12:13").param("description", "You forgot description for casts").param("relatedGame.id", "" + game.getId())).andExpect(status().isOk()).andExpect(view().name("redirect:/admin/home"));
	}

	@Test
	public void when_updating_invalid_cast_then_routed_to_form() throws Exception {
		mvc.perform(post("/cast/edit/" + cast.getId()).sessionAttr("currentGame", game.getTitle())).andExpect(status().isOk()).andExpect(view().name("redirect:/admin/home"));
	}

	@Test
	public void when_updating_valid_cast_then_routed_to_form() throws Exception {
		mvc.perform(post("/cast/edit/" + cast.getId()).sessionAttr("currentGame", game.getTitle()).param("title", "Cast Title Bis").param("videoUrl", "http://www.site.com").param("broadcastDate", "28/03/2015 12:13").param("description", "Same here")).andExpect(view().name("redirect:/admin/home"));
	}

	private void givenAtLeastOneGameIsStored() {
		game = new Game();
		game.setTitle("starcraft2");
		game.setDescription("Birds are REALLY angry this time");
		gameRepository.saveOrUpdate(game);
		assertThat(game.getId()).isGreaterThan(0L);
	}

	private void givenOneCastIsInserted() {
		cast = new Cast();
		cast.setRelatedGame(game);
		cast.setTitle("TeH cast");
		cast.setVideoUrl("http://not.what.you.think.of");
		cast.setDescription("tatatatata");
		cast.setBroadcastDate(new Date());
		cast.setViewerCount(2000000);
		cast.setLanguage("fr");
		assertThat(castRepository).isNotNull();
		castRepository.saveOrUpdate(cast);
		assertThat(cast.getId()).isGreaterThan(0L);
	}
}
