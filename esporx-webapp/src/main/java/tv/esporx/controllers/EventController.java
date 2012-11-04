package tv.esporx.controllers;

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
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.EventRepository;
import tv.esporx.repositories.OccurrenceRepository;
import tv.esporx.services.EventService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;
import static tv.esporx.framework.time.DateTimeFormat.getDefaultDateFormat;

@Controller
@RequestMapping("/event")
public class EventController {

	private static final String COMMAND = "eventCommand";
	private final OccurrenceRepository occurrenceRepository;
	private final ChannelRepository channelRepository;
    //this does not make too much sense to have both a repository and a service here
	private final EventRepository eventRepository;
	private final EventService eventService;
    private final DomainClassConverter<?> entityConverter;

	@Autowired
    public EventController(OccurrenceRepository occurrenceRepository,
                           ChannelRepository channelRepository,
                           EventRepository eventRepository,
                           EventService eventService,
                           DomainClassConverter entityConverter) {

        this.channelRepository = channelRepository;
        this.occurrenceRepository = occurrenceRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.entityConverter = entityConverter;
    }

	@ExceptionHandler({ TypeMismatchException.class,
		IllegalArgumentException.class })
	@ResponseStatus(value = NOT_FOUND)
	public ModelAndView handleExceptionArray() {
		return new ModelAndView("channel/notFound");
	}

	@RequestMapping(value = "/see/{id}", method = GET)
	public ModelAndView index(@PathVariable final long id, final HttpServletResponse response) {
		checkArgument(id > 0L);
		Event event = eventRepository.findOne(id);
		if (event == null) {
			return notFound(response, "event/notFound");
		}
		ModelMap model = new ModelMap("event", event);
		return new ModelAndView("event/index", model);
	}

	@RequestMapping(value = "/new", method = GET)
	public ModelAndView creation() {
		return populatedEventForm(new ModelAndView()).addObject(COMMAND, new Event());
	}

	@RequestMapping(value = "/edit/{eventCommand}", method = GET)
	public ModelAndView edition(@ModelAttribute(COMMAND) @PathVariable @Valid final Event eventCommand, final HttpServletResponse response, final ModelAndView modelAndView) {
		if (eventCommand == null) {
			return notFound(response, "channel/notFound");
		}
		return populatedEventForm(modelAndView);
	}

	@RequestMapping(value = { "/new", "edit/{eventCommand}" }, method = POST)
	public ModelAndView save(@ModelAttribute(COMMAND) @Valid final Event eventCommand, final BindingResult result, final ModelAndView modelAndView) {
		if (result.hasErrors()) {
			return populatedEventForm(modelAndView);
		}
		eventRepository.save(eventCommand);
		return successfulRedirectionView();
	}

	@RequestMapping(value = "/remove", method = POST)
	public ModelAndView delete(@RequestParam final long id, final HttpServletResponse response) {
		Event event = eventRepository.findOne(id);
		if (event == null) {
			return notFound(response, "channel/notFound");
		}
		eventRepository.delete(event);
		return successfulRedirectionView();
	}

	@RequestMapping(value = "/browse", method = GET)
	public ModelAndView list() {
		ModelMap model = new ModelMap();
        model.addAttribute("events", eventRepository.findAll());
		return new ModelAndView("event/list", model);
	}

	@RequestMapping(value = "/{id}/occurrences", method = GET)
	@ResponseBody
	public Collection<Occurrence> getOccurrences(@PathVariable("id") Event event) {
		checkNotNull(event, "Not a valid id");
		return occurrenceRepository.findByEvent(event);
	}

	@InitBinder(COMMAND)
	protected void customizeConversions(final WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(getDefaultDateFormat(), true));
        ((GenericConversionService) binder.getConversionService()).addConverter(entityConverter);
	}

	private ModelAndView successfulRedirectionView() {
		return new ModelAndView("redirect:/admin/home");
	}

	private ModelAndView populatedEventForm(final ModelAndView modelAndView) {
		modelAndView.setViewName("event/form");
		return modelAndView;
	}

}
