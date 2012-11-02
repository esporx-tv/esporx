package tv.esporx.controllers;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.domain.ConfigurableSlot;
import tv.esporx.repositories.ConfigurableSlotRepository;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;

@Controller
@RequestMapping("/admin/slot")
public class ConfigurableSlotController {

	private static final String COMMAND = "configurableSlotCommand";
	private final ConfigurableSlotRepository repository;
    private final DomainClassConverter<?> entityConverter;
	@Resource(name = "supportedLanguages")
	private final Set<String> allowedLocales = new HashSet<String>();

	@Autowired
    public ConfigurableSlotController(ConfigurableSlotRepository repository,
                                      DomainClassConverter<?> entityConverter) {
        this.repository = repository;
        this.entityConverter = entityConverter;
    }


	@ExceptionHandler({ TypeMismatchException.class,
		IllegalArgumentException.class })
	@ResponseStatus(value = NOT_FOUND)
	public ModelAndView handleExceptionArray(final Exception exception, final HttpServletRequest request) {
		return new ModelAndView("channel/notFound");
	}


	@RequestMapping(value = { "/new", "edit/{configurableSlotCommand}" }, method = POST)
	public ModelAndView save(@ModelAttribute(COMMAND) @Valid final ConfigurableSlot configurableSlotCommand, final BindingResult result, final HttpServletResponse httpServletResponse, final ModelAndView modelAndView) {
		if (result.hasErrors()) {
			return populatedConfigurableSlotForm(modelAndView);
		}
		repository.save(configurableSlotCommand);
		return new ModelAndView("redirect:/admin/home");
	}

	@RequestMapping(value = "/new", method = GET)
	public ModelAndView creation(final ModelAndView modelAndView) {
		ConfigurableSlot slot = new ConfigurableSlot();
		slot.setLink("http://");
		slot.setPicture("http://");
		return populatedConfigurableSlotForm(modelAndView).addObject(COMMAND, slot);
	}

	@RequestMapping(value = "/edit/{configurableSlotCommand}", method = GET)
	public ModelAndView edition(@ModelAttribute(COMMAND) @PathVariable @Valid final ConfigurableSlot configurableSlotCommand, final HttpServletResponse response, final ModelAndView modelAndView) {
		if (configurableSlotCommand == null) {
			return notFound(response, "channel/notFound");
		}
		return populatedConfigurableSlotForm(modelAndView);
	}

	@RequestMapping(value = "/remove", method = POST)
	public ModelAndView delete(@RequestParam final long id, final HttpServletResponse response) {
		ConfigurableSlot slot = repository.findOne(id);
		if (slot == null) {
			return notFound(response, "channel/notFound");
		}
		repository.delete(slot);
		return new ModelAndView("redirect:/admin/home");
	}

	@InitBinder(COMMAND)
	protected void customizeConversions(final WebDataBinder binder) {
        ((GenericConversionService) binder.getConversionService()).addConverter(entityConverter);
    }

	private ModelAndView populatedConfigurableSlotForm(final ModelAndView modelAndView) {
		modelAndView.addObject("allowedLocales", allowedLocales);
		modelAndView.setViewName("configurableSlot/form");
		return modelAndView;
	}

}
