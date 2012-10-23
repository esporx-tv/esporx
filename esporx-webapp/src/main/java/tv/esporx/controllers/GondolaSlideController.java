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
import tv.esporx.dao.PersistenceCapableGondolaSlide;
import tv.esporx.domain.front.GondolaSlide;
import tv.esporx.framework.conversion.EntityConverter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;

@Controller
@RequestMapping("/admin/slide")
public class GondolaSlideController {

	private static final String COMMAND = "gondolaSlideCommand";

	@Autowired
	private PersistenceCapableGondolaSlide gondolaDao;

	@Resource(name = "supportedLanguages")
	private final Set<String> allowedLocales = new HashSet<String>();

	@InitBinder(COMMAND)
	protected void customizeConversions(final WebDataBinder binder) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		df.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));

		EntityConverter<GondolaSlide> entityConverter = new EntityConverter<GondolaSlide>(gondolaDao, GondolaSlide.class);
		((GenericConversionService) binder.getConversionService()).addConverter(entityConverter);
	}


	@ExceptionHandler(TypeMismatchException.class)
	@ResponseStatus(value = NOT_FOUND)
	public ModelAndView handleIllegalArguments(final TypeMismatchException exception, final HttpServletRequest request) {
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
		gondolaDao.saveOrUpdate(gondolaSlideCommand);
		return new ModelAndView("redirect:/admin/home");
	}

	@RequestMapping(value = "/edit/{gondolaSlideCommand}", method = GET)
	public ModelAndView edition(@ModelAttribute(COMMAND) @PathVariable @Valid final GondolaSlide gondolaSlideCommand, final HttpServletResponse response, final ModelAndView modelAndView) {
		if (gondolaSlideCommand == null) {
			return notFound(response, "slide/notFound");
		}
		return populateModelAndView(modelAndView);
	}

	@RequestMapping(value = "/browse", method = GET)
	public ModelAndView list() {
		List<GondolaSlide> slides = gondolaDao.findByLanguage("fr");// FIXME:
		// get
		// current
		// locale
		ModelMap model = new ModelMap("gondolaSlides", slides);
		return new ModelAndView("slide/list", model);
	}

	@RequestMapping(value = "/remove", method = POST)
	public ModelAndView delete(@RequestParam final long id, final HttpServletResponse response) {
		GondolaSlide slide = gondolaDao.findById(id);
		if (slide == null) {
			return notFound(response, "channel/notFound");
		}
		gondolaDao.delete(slide);
		return new ModelAndView("redirect:/admin/home");
	}

	public void setGondolaSlideRepository(final PersistenceCapableGondolaSlide gondolaSlideDao) {
		this.gondolaDao = gondolaSlideDao;
	}

	private ModelAndView populateModelAndView(final ModelAndView modelAndView) {
		modelAndView.addObject("allowedLocales", allowedLocales);
		modelAndView.setViewName("slide/form");
		return modelAndView;
	}

}
