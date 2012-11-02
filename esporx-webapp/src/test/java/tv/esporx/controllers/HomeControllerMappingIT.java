package tv.esporx.controllers;

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
import tv.esporx.domain.Game;
import tv.esporx.framework.TestGenericWebXmlContextLoader;
import tv.esporx.repositories.GameRepository;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.server.setup.MockMvcBuilders.webApplicationContextSetup;
import static tv.esporx.filters.GameFilter.GAME_PARAMETER_NAME;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml", "classpath:/META-INF/spring/testApplicationContext.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class HomeControllerMappingIT {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private GameRepository gameRepository;
	private Game game;

	@Before
	public void setup() {
		givenAtLeastOneGameIsStored();
		mvc = webApplicationContextSetup(webApplicationContext).build();
	}

	@Test
	public void when_requesting_esporx_then_routed_to_homepage() throws Exception {
		mvc.perform(get("/home").sessionAttr(GAME_PARAMETER_NAME, game.getTitle())).andExpect(status().isOk()).andExpect(view().name("home"));
	}

	private void givenAtLeastOneGameIsStored() {
		game = new Game();
		game.setTitle("Angry nerds");
		game.setDescription("Birds are REALLY angry this time");
		gameRepository.save(game);
		assertThat(game.getId()).isGreaterThan(0L);
	}
}
