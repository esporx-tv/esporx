package tv.esporx.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.ConfigurableSlotRepository;
import tv.esporx.repositories.EventRepository;
import tv.esporx.repositories.GondolaSlideRepository;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AdminControllerTest {

	private AdminController adminController;

	@Before
	public void setup() {
		adminController = new AdminController(          //
            mock(GondolaSlideRepository.class),         //
            mock(ConfigurableSlotRepository.class),     //
            mock(ChannelRepository.class),      //
            mock(EventRepository.class)         //
        );
	}

	@Test
	public void when_trying_to_access_to_administration_then_login_page_is_retrieved() {
		ModelAndView modelAndView = adminController.login();
		assertThat(modelAndView.getViewName()).isEqualTo("admin/login");
	}

}
