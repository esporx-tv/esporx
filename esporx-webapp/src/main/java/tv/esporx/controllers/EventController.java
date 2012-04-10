package tv.esporx.controllers;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.PersistenceCapableCast;
import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.domain.Cast;
import tv.esporx.domain.Event;

@Controller
@RequestMapping("/event")
public class EventController {

	private static final String COMMAND = "eventCommand";

	@Autowired
	private PersistenceCapableEvent eventDao;
	@Autowired
	private PersistenceCapableCast castDao;

	@InitBinder(COMMAND)
	public void customizeConversions(final WebDataBinder binder) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		df.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = NOT_FOUND)
	public ModelAndView handleIllegalArguments(final IllegalArgumentException exception, final HttpServletRequest request) {
		return new ModelAndView("event/notFound");
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
		return populatedEventForm(new Event());
	}

	@RequestMapping(value = "/edit/{id}", method = GET)
	public ModelAndView edition(@PathVariable final long id, final HttpServletResponse response) {
		checkArgument(id > 0L);
		Event event = eventDao.findById(id);
		if (event == null) {
			return notFound(response, "event/notFound");
		}
		return populatedEventForm(event);
	}

	@RequestMapping(value = { "/new", "edit/{id}" }, method = POST)
	public ModelAndView save(@ModelAttribute(COMMAND) @Valid final Event event, final BindingResult result) {
		if (result.hasErrors()) {
			return populatedEventForm(event);
		}
		eventDao.saveOrUpdate(event);
		return successfulRedirectionView();
	}

	@RequestMapping(value = "/remove/{id}", method = POST)
	public ModelAndView delete(@PathVariable final long id) {
		checkArgument(id > 0);
		Event event = eventDao.findById(id);
		eventDao.delete(event);
		return successfulRedirectionView();
	}

	@RequestMapping(value = "/list", method = GET)
	public ModelAndView list() {
		ModelMap model = new ModelMap();
		List<Event> events = eventDao.findAll();
		for (Event event : events) {
			List<Cast> casts = event.getCasts();
			model.addAttribute("casts", casts);
		}
		model.addAttribute("events", events);
		return new ModelAndView("event/list", model);
	}

	@RequestMapping(value = "/link/{id}", method = GET)
	public ModelAndView link(@PathVariable final long id) {
		Event event = eventDao.findById(id);
		List<Cast> casts = castDao.findAll();
		ModelMap model = new ModelMap();
		model.addAttribute("event", event);
		model.addAttribute("casts", casts);
		return new ModelAndView("event/link", model);
	}

	@RequestMapping(value = "/link", method = POST)
	@Transactional
	public ModelAndView update(final HttpServletRequest request) {
		long castId = Long.parseLong(request.getParameter("cast"));
		Cast cast = castDao.findById(castId);
		long eventId = Long.parseLong(request.getParameter("relatedEvent"));
		Event event = eventDao.findById(eventId);
		event.addCast(cast);
		eventDao.saveOrUpdate(event);
		return successfulRedirectionView();
	}

	public void setEventRepository(final PersistenceCapableEvent eventDao) {
		this.eventDao = eventDao;
	}

	private ModelAndView successfulRedirectionView() {
		return new ModelAndView("redirect:/admin/home");
	}

	private ModelAndView populatedEventForm(final Event event) {
		ModelMap model = new ModelMap(COMMAND, event);
		return new ModelAndView("event/form", model);
	}
}
