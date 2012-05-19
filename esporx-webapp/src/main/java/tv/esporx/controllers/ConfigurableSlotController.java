package tv.esporx.controllers;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.PersistenceCapableConfigurableSlot;
import tv.esporx.domain.front.ConfigurableSlot;
import tv.esporx.framework.EntityConverter;

@Controller
@RequestMapping("/admin/slot")
public class ConfigurableSlotController {

	private static final String COMMAND = "configurableSlotCommand";

	@Autowired
	private PersistenceCapableConfigurableSlot slotDao;

	@Resource(name = "supportedLanguages")
	private final Set<String> allowedLocales = new HashSet<String>();

	@InitBinder(COMMAND)
	public void customizeConversions(final WebDataBinder binder) {

		EntityConverter<ConfigurableSlot> entityConverter = new EntityConverter<ConfigurableSlot>(slotDao, ConfigurableSlot.class);
		((GenericConversionService) binder.getConversionService()).addConverter(entityConverter);
	}

	@ExceptionHandler({ TypeMismatchException.class,
		IllegalArgumentException.class })
	@ResponseStatus(value = NOT_FOUND)
	public ModelAndView handleExceptionArray(final Exception exception, final HttpServletRequest request) {
		return new ModelAndView("cast/notFound");
	}


	@RequestMapping(value = { "/new", "edit/{configurableSlotCommand}" }, method = POST)
	public ModelAndView save(@ModelAttribute(COMMAND) @Valid final ConfigurableSlot configurableSlotCommand, final BindingResult result, final HttpServletResponse httpServletResponse, final ModelAndView modelAndView) {
		if (result.hasErrors()) {
			return populatedConfigurableSlotForm(modelAndView);
		}
		slotDao.saveOrUpdate(configurableSlotCommand);
		return new ModelAndView("redirect:/admin/home");
	}

	@RequestMapping(value = "/new", method = GET)
	public ModelAndView creation(final ModelAndView modelAndView) {
		ConfigurableSlot slot = new ConfigurableSlot();
		slot.setLink("http://");
		return populatedConfigurableSlotForm(modelAndView).addObject(COMMAND, slot);
	}

	@RequestMapping(value = "/edit/{configurableSlotCommand}", method = GET)
	public ModelAndView edition(@ModelAttribute(COMMAND) @PathVariable @Valid final ConfigurableSlot configurableSlotCommand, final HttpServletResponse response, final ModelAndView modelAndView) {
		if (configurableSlotCommand == null) {
			return notFound(response, "cast/notFound");
		}
		return populatedConfigurableSlotForm(modelAndView);
	}

	@RequestMapping(value = "/remove", method = POST)
	public ModelAndView delete(final HttpServletResponse response, final HttpServletRequest request) {
		long id = Long.parseLong(request.getParameter("id"));
		ConfigurableSlot slot = slotDao.findById(id);
		if (slot == null) {
			return notFound(response, "cast/notFound");
		}
		slotDao.delete(slot);
		return new ModelAndView("redirect:/admin/home");
	}

	public void setCastRepository(final PersistenceCapableConfigurableSlot slotDao) {
		this.slotDao = slotDao;
	}

	private ModelAndView populatedConfigurableSlotForm(final ModelAndView modelAndView) {
		modelAndView.addObject("allowedLocales", allowedLocales);
		modelAndView.setViewName("configurableSlot/form");
		return modelAndView;
	}

}
