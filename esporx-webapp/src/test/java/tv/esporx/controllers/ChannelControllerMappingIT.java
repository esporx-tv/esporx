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
import tv.esporx.domain.Channel;
import tv.esporx.domain.Game;
import tv.esporx.framework.TestGenericWebXmlContextLoader;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.GameRepository;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.server.setup.MockMvcBuilders.webApplicationContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml", "classpath:/META-INF/spring/testApplicationContext.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ChannelControllerMappingIT {

	@Autowired
	private ChannelRepository channelRepository;
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private WebApplicationContext webApplicationContext;

	private Channel channel;
	private Game game;
	private MockMvc mvc;

	@Before
	public void setup() {
		givenAtLeastOneGameIsStored();
		givenOneChannelIsInserted();
		mvc = webApplicationContextSetup(webApplicationContext).build();
	}

	@Test
	public void when_requesting_channel_creation_page_then_routed_to_form() throws Exception {
		mvc.perform(get("/channel/new")).andExpect(status().isOk()).andExpect(view().name("channel/form"));
	}

	@Test
	public void when_requesting_channel_details_page_then_routed_to_form() throws Exception {
		mvc.perform(get("/channel/watch/" + channel.getId())).andExpect(status().isOk()).andExpect(view().name("channel/index"));
	}

	@Test
	public void when_requesting_channel_details_page_with_wrong_id_then_error() throws Exception {
		mvc.perform(get("/channel/watch/-10")).andExpect(status().isNotFound()).andExpect(view().name("channel/notFound"));
	}

	@Test
	public void when_requesting_channel_edition_page_then_routed_to_form() throws Exception {
		mvc.perform(get("/channel/edit/" + channel.getId())).andExpect(status().isOk()).andExpect(view().name("channel/form"));
	}

	@Test
	public void when_requesting_channel_edition_page_with_wrong_id_then_error() throws Exception{
		mvc.perform(get("/channel/edit/-10")).andExpect(status().isNotFound()).andExpect(view().name("channel/notFound"));
	}

	@Test
	public void when_requesting_non_existing_channel_details_page_then_routed_to_not_found() throws Exception {
		mvc.perform(get("/channel/watch/" + (channel.getId() + 1000))).andExpect(status().isNotFound()).andExpect(view().name("channel/notFound"));
	}

	@Test
	public void when_requesting_non_existing_channel_edition_page_with_wrong_id_then_error() throws Exception {
		mvc.perform(get("/channel/edit/" + (channel.getId() + 1000))).andExpect(status().isNotFound()).andExpect(view().name("channel/notFound"));
	}

	@Test
	public void when_requesting_non_existing_channel_removal_then_error() throws Exception {
		mvc.perform(post("/channel/remove").param("id", "100000")).andExpect(status().isNotFound()).andExpect(view().name("channel/notFound"));
	}

	@Test
	public void when_saving_invalid_channel_then_routed_to_form() throws Exception {
		mvc.perform(post("/channel/new").sessionAttr("currentGame", game.getTitle())).andExpect(status().isOk()).andExpect(view().name("channel/form"));
	}

	@Test
	public void when_saving_valid_channel_then_routed_to_home() throws Exception {
		mvc.perform(post("/channel/new").sessionAttr("currentGame", game.getTitle()).param("title", "Channel Title").param("language", "fr").param("videoUrl", "http://www.site.com").param("broadcastDate", "28/03/2015 12:13").param("description", "You forgot description for channels").param("relatedGame.id", "" + game.getId())).andExpect(status().isOk()).andExpect(view().name("redirect:/admin/home"));
	}

	@Test
	public void when_updating_invalid_channel_then_routed_to_form() throws Exception {
		mvc.perform(post("/channel/edit/" + channel.getId()).sessionAttr("currentGame", game.getTitle())).andExpect(status().isOk()).andExpect(view().name("redirect:/admin/home"));
	}

	@Test
	public void when_updating_valid_channel_then_routed_to_form() throws Exception {
		mvc.perform(post("/channel/edit/" + channel.getId()).sessionAttr("currentGame", game.getTitle()).param("title", "Channel Title Bis").param("videoUrl", "http://www.site.com").param("broadcastDate", "28/03/2015 12:13").param("description", "Same here")).andExpect(view().name("redirect:/admin/home"));
	}

	private void givenAtLeastOneGameIsStored() {
		game = new Game();
		game.setTitle("starcraft2");
		game.setDescription("Birds are REALLY angry this time");
		gameRepository.save(game);
		assertThat(game.getId()).isGreaterThan(0L);
	}

	private void givenOneChannelIsInserted() {
		channel = new Channel();
		channel.setTitle("TeH channel");
		channel.setVideoUrl("http://not.what.you.think.of");
		channel.setDescription("tatatatata");
		channel.setViewerCount(2000000);
		channel.setLanguage("fr");
		assertThat(channelRepository).isNotNull();
		channelRepository.save(channel);
		assertThat(channel.getId()).isGreaterThan(0L);
	}
}
