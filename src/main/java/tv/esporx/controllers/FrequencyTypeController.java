package tv.esporx.controllers;

import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tv.esporx.domain.FrequencyType;
import tv.esporx.repositories.FrequencyTypeRepository;

import static com.google.common.collect.Iterables.transform;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping
public class FrequencyTypeController {

    private final FrequencyTypeRepository repository;

    @Autowired
    public FrequencyTypeController(FrequencyTypeRepository repository) {
        this.repository = repository;
    }


    @RequestMapping(value = "/admin/frequencyTypes", method = GET)
    @ResponseBody
    public Iterable<String> retrieveFrequencyTypes() {
        return transform(repository.findAll(), new Function<FrequencyType, String>() {
            @Override
            public String apply(FrequencyType type) {
                return type.getValue();
            }
        });
    }
}
