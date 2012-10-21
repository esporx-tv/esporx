package tv.esporx.controllers;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.dao.PersistenceCapableChannel;
import tv.esporx.dao.PersistenceCapableEvent;
import tv.esporx.dao.PersistenceCapableFrequencyType;
import tv.esporx.dao.PersistenceCapableOccurrence;
import tv.esporx.domain.FrequencyType;
import tv.esporx.domain.Occurrence;
import tv.esporx.framework.EntityConverter;

import javax.annotation.Nullable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static java.lang.Long.parseLong;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/occurrence")
//FIXME: this controller totally sucks
public class OccurrenceController {

    @Autowired
    private PersistenceCapableOccurrence occurrenceDao;
    @Autowired
    private PersistenceCapableEvent eventDao;
    @Autowired
    private PersistenceCapableChannel channelDao;

    @Autowired
    private PersistenceCapableFrequencyType frequencyTypeDao;

    /*@InitBinder
    public void customizeConversions(final WebDataBinder binder) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        df.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));

        EntityConverter<FrequencyType> frequencyTypeEntityConverter = new EntityConverter<FrequencyType>(frequencyTypeDao, FrequencyType.class, "findByValue", String.class);
        ((GenericConversionService) binder.getConversionService()).addConverter(frequencyTypeEntityConverter);
    }*/

    @RequestMapping(value = "", method = POST)
    @ResponseBody
    public String saveOccurrence(@RequestParam HashMap<String, Object> rawOccurrence) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        df.setLenient(false);
        try {
            Occurrence occurrence;
            String rawEventId = String.valueOf(rawOccurrence.get("data[eventId]"));
            checkNotNull(rawEventId);
            Long eventId = Long.valueOf(rawEventId);
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
            FrequencyType frequencyType = frequencyTypeDao.findByValue(String.valueOf(rawOccurrence.get("data[frequencyType]")));
            if (id != null) {
                //beware this can become a security flaw if not everyone is allowed to modify occurrences
                occurrence = occurrenceDao.findById(id);
            } else {
                occurrence = new Occurrence();
            }
            occurrence.setStartDate(startDate);
            occurrence.setEndDate(endDate);
            occurrence.setFrequencyType(frequencyType);
            return occurrenceDao.saveOrUpdate(occurrence, channelIds, eventId).toString();

        } catch (ParseException e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/new", method = GET)
    public ModelAndView showForm() {
        ModelMap model = new ModelMap();
        model.addAttribute("events", eventDao.findAll());
        return new ModelAndView("occurrence/form", model);
    }
}
