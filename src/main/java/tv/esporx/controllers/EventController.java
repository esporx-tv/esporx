package tv.esporx.controllers;

import org.slf4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.domain.Event;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.EventRepository;
import tv.esporx.repositories.OccurrenceRepository;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;
import static tv.esporx.framework.time.DateTimeFormat.getDefaultDateFormat;

@Controller
public class EventController {

    private static final Logger LOGGER = getLogger(EventController.class);
	private static final String COMMAND = "eventCommand";
	private final OccurrenceRepository occurrenceRepository;
    //this does not make too much sense to have both a repository and a service here
	private final EventRepository eventRepository;
    private final DomainClassConverter<?> entityConverter;

	@Autowired
    public EventController(OccurrenceRepository occurrenceRepository,
                           EventRepository eventRepository,
                           DomainClassConverter entityConverter) {
        this.occurrenceRepository = occurrenceRepository;
        this.eventRepository = eventRepository;
        this.entityConverter = entityConverter;
    }

	@RequestMapping(value = "/event/see/{id}", method = GET)
	public ModelAndView index(@PathVariable final long id, final HttpServletResponse response) {
		checkArgument(id > 0L);
		Event event = eventRepository.findOne(id);
		if (event == null) {
			return notFound(response, "event/notFound");
		}

        ModelMap model = new ModelMap("event", event);
		return new ModelAndView("event/index", model);
	}

    @RequestMapping(value = "/event/browse", method = GET)
    public ModelAndView list() {
        ModelMap model = new ModelMap();
        model.addAttribute("events", eventRepository.findAll());
        return new ModelAndView("event/list", model);
    }

    @RequestMapping(value = "/event/{id}/occurrences", method = GET)
    @ResponseBody
    public Collection<Occurrence> getOccurrences(@PathVariable("id") Event event) {
        checkNotNull(event, "Not a valid id");
        return occurrenceRepository.findByEvent(event);
    }

	@RequestMapping(value = "/admin/event/new", method = GET)
	public ModelAndView creation() {
		return populatedEventForm(new ModelAndView()).addObject(COMMAND, new Event());
	}

	@RequestMapping(value = "/admin/event/edit/{eventCommand}", method = GET)
	public ModelAndView edition(@ModelAttribute(COMMAND) @PathVariable @Valid final Event eventCommand, final HttpServletResponse response, final ModelAndView modelAndView) {
		if (eventCommand == null) {
			return notFound(response, "channel/notFound");
		}
		return populatedEventForm(modelAndView);
	}

	@RequestMapping(value = { "/admin/event/new", "/admin/event/edit/{eventCommand}" }, method = POST)
	public ModelAndView save(@ModelAttribute(COMMAND) @Valid final Event eventCommand, final BindingResult result, final ModelAndView modelAndView) {
		if (result.hasErrors()) {
			return populatedEventForm(modelAndView);
		}
		eventRepository.save(eventCommand);
		return successfulRedirectionView();
	}

	@RequestMapping(value = "/admin/event/remove", method = POST)
	public ModelAndView delete(@RequestParam final long id, final HttpServletResponse response) {
		Event event = eventRepository.findOne(id);
		if (event == null) {
			return notFound(response, "channel/notFound");
		}
		eventRepository.delete(event);
		return successfulRedirectionView();
	}

    @ExceptionHandler({ TypeMismatchException.class, IllegalArgumentException.class })
    @ResponseStatus(value = NOT_FOUND)
    public ModelAndView handleErrors(Exception exception) {
        LOGGER.error(exception.getMessage(), exception);
        return new ModelAndView("channel/notFound");
    }

	@InitBinder(COMMAND)
	protected void customizeConversions(final WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(getDefaultDateFormat(), true));
        ((GenericConversionService) binder.getConversionService()).addConverter(entityConverter);
	}

	private ModelAndView populatedEventForm(final ModelAndView modelAndView) {
        modelAndView.addObject("disableAnalytics", true);
        modelAndView.setViewName("event/form");
		return modelAndView;
	}

    private ModelAndView successfulRedirectionView() {
        return new ModelAndView("redirect:/admin/home?active=event");
    }

}
