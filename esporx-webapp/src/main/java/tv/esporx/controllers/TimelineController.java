package tv.esporx.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.domain.front.Timeline;
import tv.esporx.services.TimelineService;

@Controller
@RequestMapping("/timeline")
public class TimelineController {

	@Autowired
	private TimelineService timelineService;

	@RequestMapping(value = "/browse", method = GET)
	public ModelAndView index() {
		Timeline timeline = timelineService.buildTimeline(new DateTime().withDate(2012, 5, 8).withTimeAtStartOfDay());
		return new ModelAndView("timeline/timeline", new ModelMap("timeline", timeline));
	}

	public void setTimelineService(final TimelineService service) {
		timelineService = service;
	}
}
