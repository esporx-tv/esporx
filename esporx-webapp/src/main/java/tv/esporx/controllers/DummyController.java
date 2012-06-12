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
		if(activeProfiles.contains("prod")) {
			string += "prod";
		}
		else if (activeProfiles.contains("preprod")) {
			string += "preprod";
		}
		else {
			string += "default";
		}
		return string;
	}

	@Override
	public void setEnvironment(final Environment environment) {
		this.environment = environment;
	}
}
