package tv.esporx.controllers;

import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tv.esporx.dao.PersistenceCapableFrequencyType;
import tv.esporx.domain.FrequencyType;

import java.util.List;

import static com.google.common.collect.Lists.transform;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/frequencyTypes")
public class FrequencyTypeController {

    @Autowired
    private PersistenceCapableFrequencyType frequencyTypeDao;

    @RequestMapping(method = GET)
    @ResponseBody
    public List<String> retrieveFrequencyTypes() {
        return transform(frequencyTypeDao.findAll(), new Function<FrequencyType, String>() {
            @Override
            public String apply(FrequencyType type) {
                return type.getValue();
            }
        });
    }
}
