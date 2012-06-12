package tv.esporx.controllers;

import static java.util.Arrays.asList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/cloudbees-test", method = GET)
public class DummyController implements EnvironmentAware {

	private Environment environment;


	@ResponseBody
	@RequestMapping("/")
	public String test() {
		String string = "the active profile is: ";
		List<String> activeProfiles = asList(environment.getActiveProfiles());
		boolean found = false;
		if(activeProfiles.contains("prod")) {
			found = true;
			string += "prod ";
		}
		if (activeProfiles.contains("preprod")) {
			found = true;
			string += "preprod ";
		}
		if (!found) {
			string += "default";
		}
		return string;
	}

	@Override
	public void setEnvironment(final Environment environment) {
		this.environment = environment;
	}
}
