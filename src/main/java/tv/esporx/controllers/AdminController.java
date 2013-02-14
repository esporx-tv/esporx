package tv.esporx.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.repositories.ConfigurableSlotRepository;
import tv.esporx.repositories.EventRepository;
import tv.esporx.repositories.GondolaSlideRepository;

import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	private final GondolaSlideRepository slideRepository;
	private final ConfigurableSlotRepository slotRepository;
	private final ChannelRepository channelRepository;
	private final EventRepository eventRepository;

	@Autowired
    public AdminController(GondolaSlideRepository slideRepository,
                           ConfigurableSlotRepository slotRepository,
                           ChannelRepository channelRepository,
                           EventRepository eventRepository) {

        this.slideRepository = slideRepository;
        this.slotRepository = slotRepository;
        this.channelRepository = channelRepository;
        this.eventRepository = eventRepository;
    }

	@RequestMapping(value = {"/home", ""}, method = GET)
	public ModelAndView home(final Principal principal) {
		ModelMap model = new ModelMap();
        if (hasName(principal)) {
			String name = principal.getName();
			model.addAttribute("adminName", name);
			model.addAttribute("gondolas", slideRepository.findAll());
			model.addAttribute("slots", slotRepository.findAll());
			model.addAttribute("channels", channelRepository.findAll());
			model.addAttribute("events", eventRepository.findAll());
            model.addAttribute("disableAnalytics", true);
		}
		return new ModelAndView("admin/home", model);
	}

	@RequestMapping(value = "/login", method = GET)
	public ModelAndView login() {
		return new ModelAndView("admin/login", new ModelMap("disableAnalytics", true));
	}

	private boolean hasName(final Principal principal) {
		return principal != null && principal.getName() != null;
	}

}
