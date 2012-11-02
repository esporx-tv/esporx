package tv.esporx.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.server.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.server.setup.MockMvcBuilders.webApplicationContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml", "classpath:/META-INF/spring/testApplicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class AdminControllerMappingIT {

	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	public void when_accessing_login_page_then_routed_to_login_page() throws Exception {
		mvc = webApplicationContextSetup(webApplicationContext).build();
		mvc.perform(get("/admin/login")).andExpect(status().isOk()).andExpect(view().name("admin/login"));
	}

	@Test
	public void when_accessing_home_page_then_routed_to_home_page() throws Exception {
		mvc = webApplicationContextSetup(webApplicationContext).build();
		mvc.perform(get("/admin/home")).andExpect(status().isOk()).andExpect(view().name("admin/home"));
	}

}
