package tv.esporx.controllers;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static java.lang.Long.parseLong;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import tv.esporx.domain.Event;
import tv.esporx.domain.FrequencyType;
import tv.esporx.domain.Occurrence;
import tv.esporx.repositories.EventRepository;
import tv.esporx.repositories.FrequencyTypeRepository;
import tv.esporx.repositories.GameRepository;
import tv.esporx.repositories.OccurrenceRepository;
import tv.esporx.services.OccurrenceService;

import com.google.common.base.Function;
import com.google.common.base.Splitter;

@Controller
//FIXME: this controller totally sucks
public class OccurrenceController {

    private final OccurrenceRepository repository;
    private final EventRepository eventRepository;
    private final FrequencyTypeRepository frequencyTypeRepository;
    private final OccurrenceService occurrenceService;
    private final GameRepository gameRepository;
    //private final DomainClassConverter<?> entityConverter;

    @Autowired
    public OccurrenceController(OccurrenceRepository repository,
                                EventRepository eventRepository,
                                FrequencyTypeRepository frequencyTypeRepository,
                                OccurrenceService occurrenceService,
                                GameRepository gameRepository/*,
                                DomainClassConverter<?> entityConverter*/) {

        this.repository = repository;
        this.eventRepository = eventRepository;
        this.frequencyTypeRepository = frequencyTypeRepository;
		this.occurrenceService = occurrenceService;
		this.gameRepository = gameRepository;
        //this.entityConverter = entityConverter;
    }

    /*@InitBinder
    public void customizeConversions(final WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(getDefaultDateFormat(), true));
        ((GenericConversionService) binder.getConversionService()).addConverter(entityConverter);
    }*/

    @RequestMapping(value = "/admin/occurrence/manage", method = GET)
    public ModelAndView showForm() {
        ModelMap model = new ModelMap();
        model.addAttribute("events", eventRepository.findAll());
        return new ModelAndView("occurrence/form", model);
    }

    @RequestMapping(value = "/admin/occurrence", method = POST)
    @ResponseBody
    public String saveOccurrence(@RequestParam HashMap<String, Object> rawOccurrence) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        df.setLenient(false);
        try {
            Occurrence occurrence;
            String rawEventId = String.valueOf(rawOccurrence.get("data[eventId]"));
            checkNotNull(rawEventId);
            String rawId = String.valueOf(rawOccurrence.get("data[id]"));
            Long id = (rawId.isEmpty()) ? null : Long.valueOf(rawId);
            Date startDate = df.parse(String.valueOf(rawOccurrence.get("data[startDate]")));
            String rawEndDate = String.valueOf(rawOccurrence.get("data[endDate]"));
            Date endDate = rawEndDate.isEmpty() ? null : df.parse(rawEndDate);
            List<Long> channelIds = transform(
                newArrayList(Splitter.on(',').omitEmptyStrings().trimResults().split(String.valueOf(rawOccurrence.get("data[channels]")))),
                new Function<String, Long>() {
                    @Override
                    public Long apply(String rawId) {
                        return parseLong(rawId);
                    }
                }
            );
            String rawGameId = String.valueOf(rawOccurrence.get("data[gameId]"));
            checkNotNull(rawGameId);

            if (id != null) {
                //beware this can become a security flaw if not everyone is allowed to modify occurrences
                occurrence = repository.findOne(id);
            } else {
                occurrence = new Occurrence();
            }
            Event event = eventRepository.findOne(Long.valueOf(rawEventId));
            occurrence.setEvent(event);
            occurrence.setStartDate(startDate);
            occurrence.setEndDate(endDate);
            occurrence.setGame(gameRepository.findOne(Long.valueOf(rawGameId)));
            FrequencyType frequencyType = frequencyTypeRepository.findOne(String.valueOf(rawOccurrence.get("data[frequencyType]")));
            occurrence.setFrequencyType(frequencyType);
            return occurrenceService.saveWithAssociations(occurrence, channelIds).toString();

        } catch (ParseException e) {
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "/admin/occurrence/{id}", method = DELETE)
    @ResponseBody
    public String delete(@PathVariable long id) {
    	Occurrence occurrence = repository.findOne(id);
    	if (occurrence == null) {
    		return "KO";
    	}
        occurrenceService.delete(occurrence);
    	return "OK";
    	
    }
}
