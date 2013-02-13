package tv.esporx.controllers;

import org.slf4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.domain.GondolaSlide;
import tv.esporx.repositories.GondolaSlideRepository;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;
import static tv.esporx.framework.time.DateTimeFormat.getDefaultDateFormat;

@Controller
@RequestMapping("/admin/slide")
public class GondolaSlideController {

    private static final Logger LOGGER = getLogger(GondolaSlideController.class);
	private static final String COMMAND = "gondolaSlideCommand";
	private final GondolaSlideRepository repository;
    private final DomainClassConverter<?> entityConverter;
	@Resource(name = "supportedLanguages")
	private final Set<String> allowedLocales = new HashSet<String>();

	@Autowired
    public GondolaSlideController(GondolaSlideRepository repository,
                                  DomainClassConverter<?> entityConverter) {
        this.repository = repository;
        this.entityConverter = entityConverter;
    }


	@ExceptionHandler(TypeMismatchException.class)
	@ResponseStatus(value = NOT_FOUND)
	public ModelAndView handleErrors(final TypeMismatchException exception, final HttpServletRequest request) {
        LOGGER.error(exception.getMessage(), exception);
        return new ModelAndView("slide/notFound");
	}

	@RequestMapping(value = "/new", method = GET)
	public ModelAndView creation(final ModelAndView modelAndView) {
		checkNotNull(modelAndView);
		GondolaSlide slide = new GondolaSlide();
		slide.setLink("http://");
		slide.setPicture("http://");
		return populateModelAndView(modelAndView).addObject(COMMAND, slide);
	}

	@RequestMapping(value = {
			"/new",
			"/edit/{gondolaSlideCommand}"
	}, method = POST)
	public ModelAndView save(@ModelAttribute(COMMAND) @Valid final GondolaSlide gondolaSlideCommand, final BindingResult result, final ModelAndView modelAndView) {
		if (result.hasErrors()) {
			return populateModelAndView(modelAndView);
		}
		repository.save(gondolaSlideCommand);
		return successfulRedirectionView();
	}

	@RequestMapping(value = "/edit/{gondolaSlideCommand}", method = GET)
	public ModelAndView edition(@ModelAttribute(COMMAND) @PathVariable @Valid final GondolaSlide gondolaSlideCommand, final HttpServletResponse response, final ModelAndView modelAndView) {
		if (gondolaSlideCommand == null) {
			return notFound(response, "slide/notFound");
		}
		return populateModelAndView(modelAndView);
	}

	@RequestMapping(value = "/remove", method = POST)
	public ModelAndView delete(@RequestParam final long id, final HttpServletResponse response) {
		GondolaSlide slide = repository.findOne(id);
		if (slide == null) {
			return notFound(response, "channel/notFound");
		}
		repository.delete(slide);
        return successfulRedirectionView();
    }

    @InitBinder(COMMAND)
    protected void customizeConversions(final WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(getDefaultDateFormat(), true));
        ((GenericConversionService) binder.getConversionService()).addConverter(entityConverter);
    }

	private ModelAndView populateModelAndView(final ModelAndView modelAndView) {
        modelAndView.addObject("disableAnalytics", true);
		modelAndView.addObject("allowedLocales", allowedLocales);
		modelAndView.setViewName("slide/form");
		return modelAndView;
	}

    private ModelAndView successfulRedirectionView() {
        return new ModelAndView("redirect:/admin/home?active=gondola");
    }
}
