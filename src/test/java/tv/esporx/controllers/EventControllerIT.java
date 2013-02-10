package tv.esporx.controllers;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.domain.Event;
import tv.esporx.framework.TestGenericWebXmlContextLoader;
import tv.esporx.repositories.EventRepository;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, 
locations = { "classpath:esporx-servlet.xml",
	"classpath*:applicationContext.xml",
"classpath:/META-INF/spring/testApplicationContext.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class EventControllerIT {

	private BeanPropertyBindingResult bindingResult;
	private Event event;
	@Autowired
	private Validator validator;
    @Autowired
	private EventController eventController;
	private EventRepository repository;

	@Before
	public void setup() {
        givenDummyEvent();
        givenMockedEventRepository();
        setField(eventController, "eventRepository", repository);
	}

    @Test
	public void when_saving_is_successful_then_homepage_view_is_returned() {
		givenBeanHasBeenValidated();
		ModelAndView modelAndView = eventController.save(event, bindingResult, new ModelAndView() );
		assertThat(modelAndView.getViewName()).isEqualTo("redirect:/admin/home?active=event");
	}

	@Test
	public void when_saving_is_unsuccessful_then_edition_page_is_returned() {
		givenBeanHasBeenInvalidated();
		ModelAndView modelAndView = eventController.save(event, bindingResult, new ModelAndView());
		assertThat(modelAndView.getViewName()).isEqualTo("event/form");
	}

	@Test
	public void when_saving_then_is_persisted() {
		givenBeanHasBeenValidated();
		eventController.save(event, bindingResult, new ModelAndView());
		verify(repository).save(event);
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

    private void givenMockedEventRepository() {
        repository = mock(EventRepository.class);
        when(repository.findOne(anyLong())).thenReturn(event);
    }

    private void givenDummyEvent() {
        event = new Event();
        event.setTitle("EventTitle");
        event.setDescription("Hello woooooooorld");
    }
}
