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
import tv.esporx.domain.Channel;
import tv.esporx.domain.Game;
import tv.esporx.domain.VideoProvider;
import tv.esporx.framework.TestGenericWebXmlContextLoader;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.GameRepository;
import tv.esporx.repositories.VideoProviderRepository;

import javax.sql.DataSource;
import java.sql.Date;

import static com.ninja_squad.dbsetup.Operations.*;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.server.setup.MockMvcBuilders.webApplicationContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = { "classpath:esporx-servlet.xml", "classpath*:applicationContext.xml", "classpath:/META-INF/spring/testApplicationContext.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ChannelControllerMappingIT {

	@Autowired
	private ChannelRepository channelRepository;
	@Autowired
	private GameRepository gameRepository;
    @Autowired
    private VideoProviderRepository providerRepository;
	@Autowired
	private WebApplicationContext webApplicationContext;
    @Autowired
    private DataSource dataSource;

	private Channel channel;
	private Game game;
	private MockMvc mvc;

    public static final long ID = 1L;
    private static final Operation DELETE_CHANNELS =
            deleteAllFrom("games", "channels", "video_providers");
    private static final Operation INSERT_GAME =
            insertInto("games")
                    .columns("id", "description", "title", "icon_url")
                    .values(ID, "starcraft2", "Birds are REALLY angry this time", "http://i.imgur.com/YpyBYip.jpg")
                    .build();
    private static final Operation INSERT_PROVIDER =
            insertInto("video_providers")
                .columns("id", "pattern", "template", "endpoint", "case_mode")
                .values(ID, " ^(?:(?:https?)://)?(?:www.)?youtube.com/watch?(?:.*)v=([A-Za-z0-9._%-]{11}).*", "<iframe width=\"425\" height=\"349\" src=\"https://www.youtube.com/embed/{ID}\" frameborder=\"0\" allowfullscreen></iframe>", null, "INSENSITIVE")
                .build();
    private static final Operation INSERT_CHANNELS =
            sequenceOf(
                    DELETE_CHANNELS,
                    INSERT_GAME,
                    INSERT_PROVIDER,
                    insertInto("channels")
                            .columns("id", "description", "language", "title", "video_url", "viewer_count", "provider", "viewer_count_timestamp")
                            .values(ID, "tatata", "en", "TeH channel", "http://not.what.you.think.of", 1337, ID, new Date(new DateTime().getMillis()))
                            .build()
            );
    private VideoProvider videoProvider;

    @Before
	public void setup() {
        new DbSetup(new DataSourceDestination(dataSource), INSERT_CHANNELS).launch();
        channel = channelRepository.findOne(ID);
        game = gameRepository.findOne(ID);
        videoProvider = providerRepository.findOne(ID);
        mvc = webApplicationContextSetup(webApplicationContext).build();
	}

	@Test
	public void when_requesting_channel_creation_page_then_routed_to_form() throws Exception {
		mvc.perform(get("/admin/channel/new")).andExpect(status().isOk()).andExpect(view().name("channel/form"));
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
		mvc.perform(get("/admin/channel/edit/" + channel.getId())).andExpect(status().isOk()).andExpect(view().name("channel/form"));
	}

	@Test
	public void when_requesting_channel_edition_page_with_wrong_id_then_error() throws Exception{
		mvc.perform(get("/admin/channel/edit/-10")).andExpect(status().isNotFound()).andExpect(view().name("channel/notFound"));
	}

	@Test
	public void when_requesting_non_existing_channel_details_page_then_routed_to_not_found() throws Exception {
		mvc.perform(get("/channel/watch/" + (channel.getId() + 1000))).andExpect(status().isNotFound()).andExpect(view().name("channel/notFound"));
	}

	@Test
	public void when_requesting_non_existing_channel_edition_page_with_wrong_id_then_error() throws Exception {
		mvc.perform(get("/admin/channel/edit/" + (channel.getId() + 1000))).andExpect(status().isNotFound()).andExpect(view().name("channel/notFound"));
	}

	@Test
	public void when_requesting_non_existing_channel_removal_then_error() throws Exception {
		mvc.perform(post("/admin/channel/remove").param("id", "100000")).andExpect(status().isNotFound()).andExpect(view().name("channel/notFound"));
	}

	@Test
	public void when_saving_invalid_channel_then_routed_to_form() throws Exception {
		mvc.perform(post("/admin/channel/new").sessionAttr("currentGame", game.getTitle())).andExpect(status().isOk()).andExpect(view().name("channel/form"));
	}

	@Test
	public void when_saving_valid_channel_then_routed_to_home() throws Exception {
		mvc.perform(post("/admin/channel/new").sessionAttr("currentGame", game.getTitle())
                .param("description", "You forgot description for channels")
                .param("language", "fr")
                .param("title", "Channel Title")
                .param("videoUrl", "http://www.site.com")
                .param("viewerCount", "131325")
                .param("videoProvider.id", videoProvider.getId().toString()))
                .andExpect(status().isOk()).andExpect(view().name("redirect:/admin/home?active=channel"));
	}

	@Test
	public void when_updating_invalid_channel_then_routed_to_form() throws Exception {
		mvc.perform(post("/admin/channel/edit/" + channel.getId()).sessionAttr("currentGame", game.getTitle())).andExpect(status().isOk()).andExpect(view().name("redirect:/admin/home?active=channel"));
	}

	@Test
	public void when_updating_valid_channel_then_routed_to_form() throws Exception {
		mvc.perform(post("/admin/channel/edit/" + channel.getId()).sessionAttr("currentGame", game.getTitle()).param("title", "Channel Title Bis").param("videoUrl", "http://www.site.com").param("broadcastDate", "28/03/2015 12:13").param("description", "Same here")).andExpect(view().name("redirect:/admin/home?active=channel"));
	}

}
