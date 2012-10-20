package tv.esporx.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tv.esporx.dao.PersistenceCapableFrequencyType;
import tv.esporx.dao.PersistenceCapableOccurrence;
import tv.esporx.domain.FrequencyType;
import tv.esporx.domain.Occurrence;
import tv.esporx.framework.EntityConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/occurrence")
public class OccurrenceController {

    private static final String COMMAND = "eventCommand";

    @Autowired
    private PersistenceCapableOccurrence occurrenceDao;

    @Autowired
    private PersistenceCapableFrequencyType frequencyTypeDao;

    @InitBinder(COMMAND)
    public void customizeConversions(final WebDataBinder binder) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        df.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));

        EntityConverter<FrequencyType> frequencyTypeEntityConverter = new EntityConverter<FrequencyType>(frequencyTypeDao, FrequencyType.class, "findByValue", String.class);
        ((GenericConversionService) binder.getConversionService()).addConverter(frequencyTypeEntityConverter);
    }

    @RequestMapping(value = "", method = POST)
    @ResponseBody
    public Long saveOccurrence(@ModelAttribute Occurrence occurrence) {
        return occurrenceDao.saveOrUpdate(occurrence);
    }
}
