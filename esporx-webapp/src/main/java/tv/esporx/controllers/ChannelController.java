package tv.esporx.controllers;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.PersistenceCapableChannel;
import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.dao.PersistenceCapableVideoProvider;
import tv.esporx.dao.exceptions.PersistenceViolationException;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Event;
import tv.esporx.framework.EntityConverter;

@Controller
@RequestMapping("/channel")
public class ChannelController {

	private static final String COMMAND = "channelCommand";

	@Autowired
	private PersistenceCapableChannel channelDao;
	@Autowired
	private PersistenceCapableEvent eventDao;
	@Autowired
	private PersistenceCapableVideoProvider videoProvider;

	@Resource(name = "supportedLanguages")
	private final Set<String> allowedLocales = new HashSet<String>();

	@InitBinder(COMMAND)
	public void customizeConversions(final WebDataBinder binder) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		df.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));

		EntityConverter<Channel> channelConverter = new EntityConverter<Channel>(channelDao, Channel.class);
		((GenericConversionService) binder.getConversionService()).addConverter(channelConverter);

		EntityConverter<Event> eventConverter = new EntityConverter<Event>(eventDao, Event.class);
		((GenericConversionService) binder.getConversionService()).addConverter(eventConverter);
	}

	@ExceptionHandler({ TypeMismatchException.class,
		IllegalArgumentException.class })
	@ResponseStatus(value = NOT_FOUND)
	public ModelAndView handleExceptionArray(final Exception exception, final HttpServletRequest request) {
		return new ModelAndView("channel/notFound");
	}


	@RequestMapping(value = "/remove", method = POST)
	public ModelAndView delete(@RequestParam final long id, final HttpServletResponse response) {
		Channel channel = channelDao.findById(id);
		if (channel == null) {
			return notFound(response, "channel/notFound");
		}
		channelDao.delete(channel);
		return successfulRedirectionView();
	}

	@RequestMapping(value = "/watch/{id}", method = GET)
	public ModelAndView index(@PathVariable final long id, final HttpServletResponse response) {
		checkArgument(id > 0);
		Channel channel = channelDao.findById(id);
		if (channel == null) {
			return notFound(response, "channel/notFound");
		}
		ModelMap model = new ModelMap("channel", channel);
		model.addAttribute("embeddedVideo", videoProvider.getEmbeddedContents(channel.getVideoUrl()));
		return new ModelAndView("channel/index", model);
	}

	@RequestMapping(value = { "/new", "edit/{channelCommand}" }, method = POST)
	public ModelAndView save(@ModelAttribute(COMMAND) @Valid final Channel channelCommand, final BindingResult result, final HttpServletRequest request, ModelAndView modelAndView) {
		modelAndView = populatedChannelForm(modelAndView);
		try {
			if (!result.hasErrors()) {
				channelDao.saveOrUpdate(channelCommand);
				modelAndView = successfulRedirectionView();
			}
		}
		catch (PersistenceViolationException pve) {
			modelAndView.addObject("persistenceError", pve.getCauseMessage());
		}
		return modelAndView;
	}

	@RequestMapping(value = "/new", method = GET)
	public ModelAndView creation(final ModelAndView modelAndView) {
		Channel channel = new Channel();
		channel.setVideoUrl("http://");
		return populatedChannelForm(modelAndView).addObject(COMMAND, channel);
	}

	@RequestMapping(value = "/edit/{channelCommand}", method = GET)
	public ModelAndView edition(@ModelAttribute(COMMAND) @PathVariable @Valid final Channel channelCommand, final HttpServletResponse response, final ModelAndView modelAndView) {
		if (channelCommand == null) {
			return notFound(response, "channel/notFound");
		}
		return populatedChannelForm(modelAndView);
	}

	private ModelAndView successfulRedirectionView() {
		return new ModelAndView("redirect:/admin/home");
	}

	private ModelAndView populatedChannelForm(final ModelAndView modelAndView) {
		modelAndView.addObject("allowedLocales", allowedLocales);
		modelAndView.setViewName("channel/form");
		return modelAndView;
	}

	public void setVideoProvider(final PersistenceCapableVideoProvider videoProvider) {
		this.videoProvider = videoProvider;
	}
}
