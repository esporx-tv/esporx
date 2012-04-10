package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
	locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
	"classpath:/META-INF/spring/testApplicationContext.xml"})
public class ConfigurableSlotControllerIT {

	@Autowired
	private ConfigurableSlotController slotController;

	@Test
	public void when_running_suite_then_dependency_injected() {
		// when new tests, please move that to a setup method
		assertThat(slotController).isNotNull();
	}
}
