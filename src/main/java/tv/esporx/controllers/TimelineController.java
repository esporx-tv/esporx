package tv.esporx.controllers;


import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.repositories.GameRepository;
import tv.esporx.services.TimelineService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static tv.esporx.framework.time.DateTimeUtils.toEndDay;
import static tv.esporx.framework.time.DateTimeUtils.toStartDay;
import static tv.esporx.framework.time.DateTimeUtils.toStartHour;

@Controller
@RequestMapping("/calendar")
public class TimelineController {

    private final TimelineService timeline;
    private final GameRepository gameRepository;

    @Autowired
    public TimelineController(TimelineService timeline, GameRepository gameRepository) {
        this.timeline = timeline;
        this.gameRepository = gameRepository;
    }

    @RequestMapping(value = "/browse", method = GET)
    public ModelAndView browseTimeline() {
        DateTime now = new DateTime();
        return browseTimeline(toStartDay(now).toDate().getTime());
    }

    @RequestMapping(value = "/browse/start/{start}", method = GET)
    public ModelAndView browseTimeline(@PathVariable("start") Long startTime) {
        DateTime start = toStartHour(startTime == null ? new DateTime() : new DateTime(startTime));
        DateTime end = toEndDay(start.plusDays(2));//hard-code end date for now

        ModelAndView model = new ModelAndView("timeline/timeline");
        model.addObject("games", gameRepository.findAll());
        model.addObject("timeline", timeline.getTimeline(start, end).perDayAndPerHourMap().entrySet());
        model.addObject("previousStart", toStartDay(start).minusDays(3).toDate().getTime());
        model.addObject("nextStart", toStartDay(start).plusDays(3).toDate().getTime());
        return model;
    }
}
