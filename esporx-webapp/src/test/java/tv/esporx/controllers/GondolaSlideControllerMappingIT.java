package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.server.setup.MockMvcBuilders.webApplicationContextSetup;

import java.util.Date;

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

import tv.esporx.dao.PersistenceCapableGondolaSlide;
import tv.esporx.domain.front.GondolaSlide;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml", "classpath:/META-INF/spring/testApplicationContext.xml" })
@Transactional
public class GondolaSlideControllerMappingIT {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private PersistenceCapableGondolaSlide slideDao;
	private GondolaSlide slide;

	@Before
	public void setup() {
		givenDataHasBeenPurged();
		givenOneSlideHasBeenInserted();
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
		mvc.perform(post("/admin/slide/new").param("date", "28/03/2015 12:13").param("description", "blablabla").param("tagLine", "zuper tagline").param("language", "de").param("prize", "1 zillion €").param("title", "zuper title").param("link", "http://www.google.cn").param("picture", "yourface.gif")).andExpect(status().isOk()).andExpect(view().name("redirect:/admin/home"));
	}

	@Test
	public void when_requesting_invalid_gondola_slide_edition_page_then_routed_to_not_found() throws Exception {
		mvc.perform(get("/admin/slide/edit/-4")).andExpect(status().isNotFound()).andExpect(view().name("slide/notFound"));
	}

	@Test
	public void when_requesting_valid_gondola_slide_edition_page_then_routed_to_not_found() throws Exception {
		mvc.perform(get("/admin/slide/edit/" + slide.getId())).andExpect(status().isOk()).andExpect(view().name("slide/form"));
	}

	@Test
	public void when_requesting_non_existing_gondola_slide_edition_page_then_routed_to_not_found() throws Exception {
		mvc.perform(get("/admin/slide/edit/" + (slide.getId() + 1000L))).andExpect(status().isNotFound()).andExpect(view().name("slide/notFound"));
	}

	@Test
	public void when_requesting_gondola_slides_then_routed_to_list_page() throws Exception {
		mvc.perform(get("/admin/slide/browse")).andExpect(status().isOk()).andExpect(view().name("slide/list"));
	}

	private void givenDataHasBeenPurged() {
		entityManager.createNativeQuery("delete from gondola_slides").executeUpdate();
	}

	private void givenOneSlideHasBeenInserted() {
		slide = new GondolaSlide();
		slide.setDate(new Date());
		slide.setDescription("blablablabla");
		slide.setLanguage("fr");
		slide.setLink("http://www.esporx.kr");
		slide.setPicture("inyourface.gif");
		slide.setPrize("$10");
		slide.setTagLine("Incredible!");
		slide.setTitle("Oh yeah title");
		assertThat(slideDao).isNotNull();
		slideDao.saveOrUpdate(slide);
		assertThat(slide.getId()).isGreaterThan(0L);
	}
}
