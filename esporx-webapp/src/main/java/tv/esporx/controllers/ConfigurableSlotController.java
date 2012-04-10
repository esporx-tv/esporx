package tv.esporx.controllers;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.PersistenceCapableConfigurableSlot;
import tv.esporx.domain.front.ConfigurableSlot;

@Controller
@RequestMapping("/admin/slot")
public class ConfigurableSlotController {

	private static final String COMMAND = "configurableSlotCommand";

	@Autowired
	private PersistenceCapableConfigurableSlot slotDao;

	@RequestMapping(value = { "/new", "edit/{id}" }, method = POST)
	public ModelAndView save(@ModelAttribute(COMMAND) @Valid ConfigurableSlot slot, BindingResult result) {
		if (result.hasErrors()) {
			return populatedConfigurableSlotForm(slot);
		}
		slotDao.saveOrUpdate(slot);
		return new ModelAndView("redirect:/admin/home");
	}

	@RequestMapping(value = "/new", method = GET)
	public ModelAndView creation() {
		return populatedConfigurableSlotForm(new ConfigurableSlot());
	}

	@RequestMapping(value = "/edit/{id}", method = GET)
	public ModelAndView edition(@PathVariable final long id, HttpServletResponse response) {
		checkArgument(id > 0L);
		ConfigurableSlot slot = slotDao.findById(id);
		if (slot == null) {
			return notFound(response, "configurableSlot/notFound");
		}
		return populatedConfigurableSlotForm(slot);
	}

	@RequestMapping(value = "/browse", method = GET)
	public ModelAndView list() {
		List<ConfigurableSlot> slots = slotDao.findAll();
		ModelMap model = new ModelMap();
		model.addAttribute("configurableSlots", slots);
		return new ModelAndView("configurableSlot/list", model);
	}

	public void setCastRepository(final PersistenceCapableConfigurableSlot slotDao) {
		this.slotDao = slotDao;
	}

	private ModelAndView populatedConfigurableSlotForm(final ConfigurableSlot slot) {
		ModelMap model = new ModelMap();
		model.addAttribute(COMMAND, slot);
		return new ModelAndView("configurableSlot/form", model);
	}

}
