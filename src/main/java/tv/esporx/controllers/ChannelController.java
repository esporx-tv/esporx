package tv.esporx.controllers;

import com.google.common.base.Function;
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
import tv.esporx.conversion.VideoProviderConverter;
import tv.esporx.domain.Channel;
import tv.esporx.domain.remote.JsonChannel;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.VideoProviderRepository;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.transform;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;
import static tv.esporx.framework.time.DateTimeFormat.getDefaultDateFormat;

@Controller
@RequestMapping("/channel")
public class ChannelController {

	private static final String COMMAND = "channelCommand";
	private final ChannelRepository repository;
	private final VideoProviderRepository videoProviderRepository;
    private final DomainClassConverter<?> entityConverter;
	@Resource(name = "supportedLanguages")
	private final Set<String> allowedLocales = new HashSet<String>();

	@Autowired
    public ChannelController(ChannelRepository repository,
                             VideoProviderRepository videoProviderRepository,
                             DomainClassConverter<?> entityConverter) {

        this.repository = repository;
        this.videoProviderRepository = videoProviderRepository;
        this.entityConverter = entityConverter;
    }


	@ExceptionHandler({ TypeMismatchException.class,
		IllegalArgumentException.class })
	@ResponseStatus(value = NOT_FOUND)
	public ModelAndView handleExceptionArray(final Exception exception, final HttpServletRequest request) {
		return new ModelAndView("channel/notFound");
	}

	@RequestMapping(value = "/remove", method = POST)
	public ModelAndView delete(@RequestParam final long id, final HttpServletResponse response) {
		Channel channel = repository.findOne(id);
		if (channel == null) {
			return notFound(response, "channel/notFound");
		}
		repository.delete(channel);
		return successfulRedirectionView();
	}

	@RequestMapping(value = "/watch/{id}", method = GET)
	public ModelAndView index(@PathVariable final long id, final HttpServletResponse response) {
		checkArgument(id > 0);
		Channel channel = repository.findOne(id);
		if (channel == null) {
			return notFound(response, "channel/notFound");
		}
		ModelMap model = new ModelMap("channel", channel);
		model.addAttribute("embeddedVideo", videoProviderRepository.getEmbeddedContents(channel.getVideoUrl()));
		return new ModelAndView("channel/index", model);
	}

	@RequestMapping(value = { "/new", "edit/{channelCommand}" }, method = POST)
	public ModelAndView save(@ModelAttribute(COMMAND) @Valid final Channel channelCommand, final BindingResult result, ModelAndView modelAndView) {
		modelAndView = populatedChannelForm(modelAndView);
		try {
			if (!result.hasErrors()) {
				repository.save(channelCommand);
				modelAndView = successfulRedirectionView();
			}
		}
		catch (PersistenceException pve) {
			modelAndView.addObject("persistenceError", pve.getMessage());
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

    @RequestMapping(value = "/all", method = GET)
    @ResponseBody
    public Iterable<JsonChannel> retrieveChannels() {
        return transform(repository.findAll(), new Function<Channel, JsonChannel>() {
            @Override
            public JsonChannel apply(Channel channel) {
                JsonChannel jsonChannel = new JsonChannel();
                jsonChannel.id = channel.getId();
                jsonChannel.name = channel.getTitle();
                return jsonChannel;
            }
        });
    }

	@InitBinder(COMMAND)
	protected void customizeConversions(final WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(getDefaultDateFormat(), true));
        GenericConversionService genericConversionService = (GenericConversionService) binder.getConversionService();
        genericConversionService.addConverter(entityConverter);
        genericConversionService.addConverter(new VideoProviderConverter(videoProviderRepository));
    }

	private ModelAndView successfulRedirectionView() {
		return new ModelAndView("redirect:/admin/home");
	}

	private ModelAndView populatedChannelForm(final ModelAndView modelAndView) {
		modelAndView.addObject("allowedLocales", allowedLocales);
        modelAndView.addObject("disableAnalytics", true);
		modelAndView.setViewName("channel/form");
		return modelAndView;
	}
}