package tv.esporx.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.impl.EventRepository;
import tv.esporx.domain.Event;
import tv.esporx.framework.TestGenericWebXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "file:src/main/webapp/WEB-INF/esporx-servlet.xml", 
	"file:src/main/webapp/WEB-INF/applicationContext.xml", 
"classpath:/META-INF/spring/testApplicationContext.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class EventControllerIT {

	private BeanPropertyBindingResult bindingResult;
	private Event event;
	@Autowired
	private EventController eventController;
	private EventRepository eventDao;

	@Autowired
	private Validator validator;
	private HttpServletRequest request;

	@Before
	public void setup() {
		eventDao = mock(EventRepository.class);
		event = new Event();
		event.setTitle("EventTitle");
		event.setDescription("Hello woooooooorld");
		event.setStartDate(new Date(0L));
		event.setEndDate(new Date());
		when(eventDao.findById(anyInt())).thenReturn(event);
		assertThat(eventController).isNotNull();
		request = mock(HttpServletRequest.class);
		eventController.setEventRepository(eventDao);
	}

	@Test
	public void when_saving_is_successful_then_homepage_view_is_returned() {
		givenBeanHasBeenValidated();
		ModelAndView modelAndView = eventController.save(event, bindingResult, request, new ModelAndView() );
		assertThat(modelAndView.getViewName()).isEqualTo("redirect:/admin/home");
	}

	@Test
	public void when_saving_is_unsuccessful_then_edition_page_is_returned() {
		givenBeanHasBeenInvalidated();
		ModelAndView modelAndView = eventController.save(event, bindingResult, request, new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("event/form");
	}

	@Test
	public void when_saving_then_is_persisted() {
		givenBeanHasBeenValidated();
		eventController.save(event, bindingResult, request, new ModelAndView());
		verify(eventDao).saveOrUpdate(event);
	}

	private void givenBeanHasBeenInvalidated() {
		event = new Event();
		bindingResult = new BeanPropertyBindingResult(event, "eventCommand");
		validator.validate(event, bindingResult);
	}

	private void givenBeanHasBeenValidated() {
		bindingResult = new BeanPropertyBindingResult(event, "eventCommand");
		validator.validate(event, bindingResult);
	}

}
