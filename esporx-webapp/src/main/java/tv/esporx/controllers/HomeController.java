package tv.esporx.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.PersistenceCapableCast;
import tv.esporx.dao.PersistenceCapableConfigurableSlot;
import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.dao.PersistenceCapableGame;
import tv.esporx.dao.PersistenceCapableGondolaSlide;
import tv.esporx.dao.impl.EventRepository;
import tv.esporx.domain.Cast;
import tv.esporx.domain.Event;
import tv.esporx.domain.Game;
import tv.esporx.domain.front.ConfigurableSlot;
import tv.esporx.domain.front.GondolaSlide;
import tv.esporx.framework.mvc.RequestUtils;

@Controller
@RequestMapping("/home")
public class HomeController {

	@Autowired
	private PersistenceCapableCast castDao;
	@Autowired
	private PersistenceCapableEvent eventDao;
	@Autowired
	private PersistenceCapableGame gameDao;
	@Autowired
	private PersistenceCapableGondolaSlide gondolaDao;
	@Autowired
	private PersistenceCapableConfigurableSlot slotDao;

	@Autowired
	private RequestUtils requestHelper;

	@RequestMapping(method = GET)
	public ModelAndView index(final HttpServletRequest incomingRequest) {
		List<Cast> mostViewedCasts = castDao.findMostViewed();
		List<Event> mostViewedEvents = eventDao.findAll();
		List<Event> upNextEvents = eventDao.findUpNext(new DateTime());
		String currentLocale = requestHelper.currentLocale(incomingRequest);
		List<GondolaSlide> slides = gondolaDao.findByLanguage(currentLocale);
		List<ConfigurableSlot> slots = slotDao.findByLanguage(currentLocale);
		ModelMap model = new ModelMap("mostViewedEvents", mostViewedEvents);
		model.addAttribute("mostViewedCasts", mostViewedCasts);
		model.addAttribute("upNextEvents", upNextEvents);
		model.addAttribute("gondolaSlides", slides);
		model.addAttribute("slots", slots);
		Game game = gameDao.findByTitle(requestHelper.currentGame(incomingRequest));
		model.addAttribute("game", game);
		return new ModelAndView("home", model);
	}

	public void setCastRepository(final PersistenceCapableCast castDao) {
		this.castDao = castDao;
	}

	public void setEventRepository(final EventRepository eventDao) {
		this.eventDao = eventDao;
	}

	public void setGameRepository(final PersistenceCapableGame gameDao) {
		this.gameDao = gameDao;
	}

	public void setGondolaRepository(final PersistenceCapableGondolaSlide gondolaDao) {
		this.gondolaDao = gondolaDao;
	}

	public void setSlotRepository(final PersistenceCapableConfigurableSlot slotDao) {
		this.slotDao = slotDao;
	}

	public void setRequestHelper(final RequestUtils requestHelper) {
		this.requestHelper = requestHelper;
	}
}
