package tv.esporx.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.framework.mvc.RequestUtils;
import tv.esporx.repositories.ConfigurableSlotRepository;
import tv.esporx.repositories.GondolaSlideRepository;
import tv.esporx.services.EventService;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class HomeController {

	private final GondolaSlideRepository gondolaRepository;
	private final ConfigurableSlotRepository slotRepository;
	private final RequestUtils requestHelper;
	private final EventService eventService;

    @Autowired
    public HomeController(GondolaSlideRepository gondolaRepository,
                          ConfigurableSlotRepository slotRepository,
                          RequestUtils requestHelper,
                          EventService eventService) {

        this.gondolaRepository = gondolaRepository;
        this.slotRepository = slotRepository;
        this.requestHelper = requestHelper;
        this.eventService = eventService;
    }


    @RequestMapping({"", "/"})
	public ModelAndView landing() {
		return new ModelAndView("index");
	}
	
	@RequestMapping(value="/home", method = GET)
	public ModelAndView index(final HttpServletRequest incomingRequest) {
        String currentLocale = requestHelper.currentLocale(incomingRequest);
        ModelMap model = new ModelMap("mostViewedEvents", eventService.findMostViewed());
        model.addAttribute("liveNowEvents", eventService.findLiveNow());
		model.addAttribute("upNextEvents", eventService.findUpNext());
		model.addAttribute("gondolaSlides", gondolaRepository.findByLanguage(currentLocale));
		model.addAttribute("slots", slotRepository.findByLanguageOrderByPositionAsc(currentLocale));
		return new ModelAndView("home", model);
	}
	
	
}
