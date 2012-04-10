package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

public class AdminControllerTest {

	private AdminController adminController;

	@Before
	public void setup() {
		adminController = new AdminController();
	}

	@Test
	public void when_trying_to_access_to_administration_then_login_page_is_retrieved() {
		ModelAndView modelAndView = adminController.login();
		assertThat(modelAndView.getViewName()).isEqualTo("admin/login");
	}

}
