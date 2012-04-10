package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.server.setup.MockMvcBuilders.webApplicationContextSetup;
import static tv.esporx.filters.GameFilter.GAME_PARAMETER_NAME;

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

import tv.esporx.dao.PersistenceCapableGame;
import tv.esporx.domain.Game;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml", "classpath:/META-INF/spring/testApplicationContext.xml" })
@Transactional
public class HomeControllerMappingIT {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private PersistenceCapableGame gameRepository;
	@PersistenceContext
	private EntityManager entityManager;
	private Game game;

	@Before
	public void setup() {
		givenDataHasBeenPurged();
		givenAtLeastOneGameIsStored();
		mvc = webApplicationContextSetup(webApplicationContext).build();
	}

	@Test
	public void when_requesting_esporx_then_routed_to_homepage() throws Exception {
		mvc.perform(get("/home").sessionAttr(GAME_PARAMETER_NAME, game.getTitle())).andExpect(status().isOk()).andExpect(view().name("home"));
	}

	private void givenDataHasBeenPurged() {
		entityManager.createNativeQuery("delete from games").executeUpdate();
		entityManager.createNativeQuery("delete from casts").executeUpdate();
		entityManager.createNativeQuery("delete from events").executeUpdate();
		entityManager.createNativeQuery("delete from gondola_slides").executeUpdate();
		entityManager.createNativeQuery("delete from configurable_slots").executeUpdate();
	}

	private void givenAtLeastOneGameIsStored() {
		game = new Game();
		game.setTitle("Angry nerds");
		game.setDescription("Birds are REALLY angry this time");
		gameRepository.saveOrUpdate(game);
		assertThat(game.getId()).isGreaterThan(0L);
	}
}
