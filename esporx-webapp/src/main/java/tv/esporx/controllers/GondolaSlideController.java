package tv.esporx.controllers;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.PersistenceCapableGondolaSlide;
import tv.esporx.domain.front.GondolaSlide;
import tv.esporx.framework.EntityConverter;

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
		return populateModelAndView(modelAndView).addObject(COMMAND, new GondolaSlide());
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

	public void setGondolaSlideRepository(final PersistenceCapableGondolaSlide gondolaSlideDao) {
		this.gondolaDao = gondolaSlideDao;
	}

	private ModelAndView populateModelAndView(final ModelAndView modelAndView) {
		modelAndView.addObject("allowedLocales", allowedLocales);
		modelAndView.setViewName("slide/form");
		return modelAndView;
	}

}
