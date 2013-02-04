package tv.esporx.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "classpath:esporx-servlet.xml",
	"classpath*:applicationContext.xml",
"classpath:/META-INF/spring/testApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class HomeControllerIT {

	@Autowired
	private HomeController homeController;

	@Before
	public void setup() {
		assertThat(homeController).isNotNull();
	}

	@Test
	public void when_triggering_a_test_then_assertion_in_setup_works() {
		// ;P
	}
}