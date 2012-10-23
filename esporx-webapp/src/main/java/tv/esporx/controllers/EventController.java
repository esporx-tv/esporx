package tv.esporx.controllers;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.dao.PersistenceCapableChannel;
import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.dao.PersistenceCapableOccurrence;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Event;
import tv.esporx.domain.Occurrence;
import tv.esporx.framework.conversion.EntityConverter;
import tv.esporx.services.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Long.parseLong;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;

@Controller
@RequestMapping("/event")
public class EventController {

	private static final String COMMAND = "eventCommand";

	@Autowired
	private PersistenceCapableEvent eventDao;
	@Autowired
	private PersistenceCapableChannel channelDao;
	@Autowired
	private PersistenceCapableOccurrence occurrenceDao;
	@Autowired
	private EventService eventService;

	@InitBinder(COMMAND)
	public void customizeConversions(final WebDataBinder binder) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		df.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));

		EntityConverter<Event> entityConverter = new EntityConverter<Event>(eventDao, Event.class);
		((GenericConversionService) binder.getConversionService()).addConverter(entityConverter);
	}

	@ExceptionHandler({ TypeMismatchException.class,
		IllegalArgumentException.class })
	@ResponseStatus(value = NOT_FOUND)
	public ModelAndView handleExceptionArray(final Exception exception, final HttpServletRequest request) {
		return new ModelAndView("channel/notFound");
	}

	@RequestMapping(value = "/see/{id}", method = GET)
	public ModelAndView index(@PathVariable final long id, final HttpServletResponse response) {
		checkArgument(id > 0L);
		Event event = eventDao.findById(id);
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
	public ModelAndView save(@ModelAttribute(COMMAND) @Valid final Event eventCommand, final BindingResult result, final HttpServletRequest request, final ModelAndView modelAndView) {
		if (result.hasErrors()) {
			return populatedEventForm(modelAndView);
		}
		eventDao.saveOrUpdate(eventCommand);
		return successfulRedirectionView();
	}

	@RequestMapping(value = "/remove", method = POST)
	public ModelAndView delete(@RequestParam final long id, final HttpServletResponse response) {
		Event event = eventDao.findById(id);
		if (event == null) {
			return notFound(response, "channel/notFound");
		}
		eventDao.delete(event);
		return successfulRedirectionView();
	}

	@RequestMapping(value = "/browse", method = GET)
	public ModelAndView list() {
		ModelMap model = new ModelMap();
		List<Event> events = eventDao.findAll();
		model.addAttribute("events", events);
		return new ModelAndView("event/list", model);
	}

	@RequestMapping(value = "/link/{id}", method = GET)
	public ModelAndView link(@PathVariable final long id) {
		Event event = eventDao.findById(id);
		List<Channel> channels = channelDao.findAll();
		ModelMap model = new ModelMap();
		model.addAttribute("event", event);
		model.addAttribute("channels", channels);
		return new ModelAndView("event/link", model);
	}

	@RequestMapping(value = "/link", method = POST)
	public ModelAndView update(final HttpServletRequest request) {
		long channelId = parseLong(request.getParameter("channel"));
		long eventId = parseLong(request.getParameter("relatedEvent"));
		eventService.saveOrUpdate(channelId, eventId);
		return successfulRedirectionView();
	}
	
	@RequestMapping(value = "/{id}/occurrences", method = GET)
	@ResponseBody
	public Collection<Occurrence> getOccurrences(@PathVariable("id") Long id) {
		checkArgument(id > 0, "Not a valid id");
		return occurrenceDao.findByEventId(id);
	}
	
	public void setEventRepository(final PersistenceCapableEvent eventDao) {
		this.eventDao = eventDao;
	}

	private ModelAndView successfulRedirectionView() {
		return new ModelAndView("redirect:/admin/home");
	}

	private ModelAndView populatedEventForm(final ModelAndView modelAndView) {
		modelAndView.setViewName("event/form");
		return modelAndView;
	}

}
