package tv.esporx.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.domain.front.Timeline;
import tv.esporx.domain.front.TimelineDimensions;
import tv.esporx.services.TimelineService;

@Controller
@RequestMapping("/calendar")
public class TimelineController {

	@Autowired
	private TimelineService timelineService;
	private final TimelineDimensions timelineDimensions = new TimelineDimensions();

	@RequestMapping(value = "/browse", method = GET)
	public ModelAndView index() {
		DateTime currentDate = new DateTime();
		Timeline timeline = timelineService.buildTimeline(currentDate.withTimeAtStartOfDay(), timelineDimensions);
		ModelMap model = new ModelMap("timeline", timeline);
		model.addAttribute("currentTimestamp", currentDate.getMillis());
		return new ModelAndView("timeline/timeline", model);
	}

	@RequestMapping(value = "/shift/{direction}/{timestamp}", method = GET)
	//TODO: create dedicated enum for direction
	public ModelAndView shift(@PathVariable("direction") final String direction, @PathVariable("timestamp") final long timestamp) {
		DateTime currentDate = determineStartDate(direction, timestamp);
		Timeline timeline = timelineService.buildTimeline(currentDate.withTimeAtStartOfDay());
		ModelMap model = new ModelMap("timeline", timeline);
		model.addAttribute("currentTimestamp", currentDate.getMillis());
		return new ModelAndView("timeline/timeline", model);
	}

	// TODO: create dedicated enum for direction
	private DateTime determineStartDate(final String direction, final long timestamp) {
		DateTime date;
		int dayOffset = timelineDimensions.getMaxWidth().asMilliseconds() / (24 * 60 * 60 * 1000);
		if (direction.equals("past")) {
			date = new DateTime(timestamp).minusDays(dayOffset);
		}
		else if (direction.equals("future")) {
			date = new DateTime(timestamp).plusDays(dayOffset);
		}
		else {
			throw new IllegalArgumentException("Not valid direction");
		}
		return date.withTimeAtStartOfDay();
	}

}
