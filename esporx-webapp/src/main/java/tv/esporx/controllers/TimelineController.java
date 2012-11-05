package tv.esporx.controllers;


import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.services.TimelineService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static tv.esporx.framework.time.DateTimeUtils.toEndDay;
import static tv.esporx.framework.time.DateTimeUtils.toStartDay;

@Controller
@RequestMapping("/calendar")
public class TimelineController {

    private final TimelineService timeline;

    @Autowired
    public TimelineController(TimelineService timeline) {
        this.timeline = timeline;
    }

    @RequestMapping(value = "/browse", method = GET)
    public ModelAndView browseTimeline() {
        DateTime now = new DateTime();
        return browseTimeline(toStartDay(now).toDate().getTime());
    }

    @RequestMapping(value = "/browse/start/{start}", method = GET)
    public ModelAndView browseTimeline(@PathVariable("start") Long startTime) {
        DateTime start = startTime == null ? new DateTime() : new DateTime(startTime);
        DateTime end = toEndDay(start.plusDays(2));//hard-code end date for now
        ModelAndView model = new ModelAndView("timeline/timeline");
        model.addObject("timeline", timeline.getTimeline().occurrencesPerDaysAt(start, end));
        model.addObject("previousStart", toStartDay(start).minusDays(3).toDate().getTime());
        model.addObject("nextStart", toStartDay(start).plusDays(3).toDate().getTime());
        return model;
    }


}
