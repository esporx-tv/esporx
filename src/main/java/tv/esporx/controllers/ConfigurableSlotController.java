package tv.esporx.controllers;

import com.google.common.collect.*;
import org.slf4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.collections.SlotOrganizer;
import tv.esporx.collections.functions.ByIdConfigurableSlotIndexer;
import tv.esporx.domain.ConfigurableSlot;
import tv.esporx.domain.front.JsonSlot;
import tv.esporx.repositories.ConfigurableSlotRepository;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Maps.uniqueIndex;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static tv.esporx.framework.mvc.ControllerUtils.notFound;

@Controller
@RequestMapping("/admin/slot")
public class ConfigurableSlotController {

    private static final Logger LOGGER = getLogger(ConfigurableSlotController.class);
    private static final String COMMAND = "configurableSlotCommand";
    private final ConfigurableSlotRepository repository;
    private final DomainClassConverter<?> entityConverter;
    @Resource(name = "supportedLanguages")
    private final Set<String> allowedLocales = new HashSet<String>();

    @Autowired
    public ConfigurableSlotController(ConfigurableSlotRepository repository,
                                      DomainClassConverter<?> entityConverter) {
        this.repository = repository;
        this.entityConverter = entityConverter;
    }


    @ExceptionHandler({ TypeMismatchException.class,
        IllegalArgumentException.class })
    @ResponseStatus(value = NOT_FOUND)
    public ModelAndView handleErrors(final Exception exception, final HttpServletRequest request) {
        LOGGER.error(exception.getMessage(), exception);
        return new ModelAndView("slot/notFound");
    }


    @RequestMapping(value = { "/new", "edit/{configurableSlotCommand}" }, method = POST)
    public ModelAndView save(@ModelAttribute(COMMAND) @Valid final ConfigurableSlot configurableSlotCommand, final BindingResult result, final HttpServletResponse httpServletResponse, final ModelAndView modelAndView) {
        if (result.hasErrors()) {
            return populatedConfigurableSlotForm(modelAndView);
        }
        repository.save(configurableSlotCommand);
        return successfulRedirectionView();
    }

    @RequestMapping(value = "/new", method = GET)
    public ModelAndView creation(final ModelAndView modelAndView) {
        ConfigurableSlot slot = new ConfigurableSlot();
        slot.setLink("http://");
        slot.setPicture("http://");
        return populatedConfigurableSlotForm(modelAndView).addObject(COMMAND, slot);
    }

    @RequestMapping(value= "/layout", method = GET)
    public ModelAndView layOut(final ModelAndView modelAndView) {
        List<ConfigurableSlot> slots = repository.findByLanguageAndActiveOrderByOrdinateAsc("en", true);
        modelAndView.setViewName("configurableSlot/organize");
        modelAndView.addObject("slots", new SlotOrganizer(slots).reorganize().slots());
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value= "/layout", method = POST)
    public Collection<Long> layOut(@RequestBody JsonSlot[] slots) {

        List<JsonSlot> slotsList = Lists.newArrayList(slots);
        Iterable<ConfigurableSlot> configurableSlots = repository.findAll(transform(slotsList, new ByIdConfigurableSlotIndexer()));
        Map<Long,JsonSlot> longJsonSlot = uniqueIndex(slotsList, new ByIdConfigurableSlotIndexer());

        for (ConfigurableSlot configurableSlot : configurableSlots) {
            JsonSlot jsonSlot = longJsonSlot.get(configurableSlot.getId());
            configurableSlot.setAbscissa(jsonSlot.getAbscissa());
            configurableSlot.setOrdinate(jsonSlot.getOrdinate());
            configurableSlot.setWidth(jsonSlot.getWidth());
            configurableSlot.setActive(true);
        }
        repository.save(configurableSlots);

        return longJsonSlot.keySet();
    }

    @RequestMapping(value = "/edit/{configurableSlotCommand}", method = GET)
    public ModelAndView edition(@ModelAttribute(COMMAND) @PathVariable @Valid final ConfigurableSlot configurableSlotCommand, final HttpServletResponse response, final ModelAndView modelAndView) {
        if (configurableSlotCommand == null) {
            return notFound(response, "channel/notFound");
        }
        return populatedConfigurableSlotForm(modelAndView);
    }

    @RequestMapping(value = "/remove", method = POST)
    public ModelAndView delete(@RequestParam final long id, final HttpServletResponse response) {
        ConfigurableSlot slot = repository.findOne(id);
        if (slot == null) {
            return notFound(response, "channel/notFound");
        }
        repository.delete(slot);
        return successfulRedirectionView();
    }

    @InitBinder(COMMAND)
    protected void customizeConversions(final WebDataBinder binder) {
        ((GenericConversionService) binder.getConversionService()).addConverter(entityConverter);
    }

    private ModelAndView populatedConfigurableSlotForm(final ModelAndView modelAndView) {
        modelAndView.addObject("allowedLocales", allowedLocales);
        modelAndView.addObject("disableAnalytics", true);
        modelAndView.setViewName("configurableSlot/form");
        return modelAndView;
    }

    private ModelAndView successfulRedirectionView() {
        return new ModelAndView("redirect:/admin/home?active=slot");
    }

}
