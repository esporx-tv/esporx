package tv.esporx.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.domain.Cast;
import tv.esporx.domain.Event;

@Controller
@RequestMapping("/timeline")
public class TimeLineController {

	@Autowired
	private PersistenceCapableEvent eventDao;

	@RequestMapping(method = GET)
	public ModelAndView index() {
		List<Event> events = eventDao.findTimeLine(new DateTime(), new DateTime().plusDays(3));
		ModelMap model = new ModelMap("events", events);
		for (Event event : events) {
			List<Cast> casts = event.getCasts();
			model.addAttribute("casts", casts);
		}
		return new ModelAndView("timeline/timeline", model);

	}

}
