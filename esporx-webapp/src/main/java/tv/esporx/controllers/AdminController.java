package tv.esporx.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.dao.PersistenceCapableChannel;
import tv.esporx.dao.PersistenceCapableConfigurableSlot;
import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.dao.PersistenceCapableGondolaSlide;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Event;
import tv.esporx.domain.front.ConfigurableSlot;
import tv.esporx.domain.front.GondolaSlide;

import java.security.Principal;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired
	private PersistenceCapableGondolaSlide gondolaDao;
	@Autowired
	private PersistenceCapableConfigurableSlot slotDao;
	@Autowired
	private PersistenceCapableChannel channelDao;
	@Autowired
	private PersistenceCapableEvent eventDao;

	@RequestMapping(value = "/home", method = GET)
	public ModelAndView home(final Principal principal) {
		ModelMap model = new ModelMap();
		List<GondolaSlide> gondolas = gondolaDao.findAll();
		List<ConfigurableSlot> slots = slotDao.findAll();
		List<Channel> channels = channelDao.findAll();
		List<Event> events = eventDao.findAll();
		if (principalHasAName(principal)) {
			String name = principal.getName();
			model.addAttribute("adminName", name);
			model.addAttribute("gondolas", gondolas);
			model.addAttribute("slots", slots);
			model.addAttribute("channels", channels);
			model.addAttribute("events", events);
		}
		return new ModelAndView("admin/home", model);
	}

	private boolean principalHasAName(final Principal principal) {
		// danger: this deeply sucks
		return principal != null && principal.getName() != null;
	}

	@RequestMapping(value = "/login", method = GET)
	public ModelAndView login() {
		return new ModelAndView("admin/login");
	}

}
