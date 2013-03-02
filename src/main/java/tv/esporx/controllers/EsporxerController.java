package tv.esporx.controllers;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tv.esporx.domain.Esporxer;
import tv.esporx.framework.security.HashGenerator;
import tv.esporx.repositories.EsporxerRepository;
import tv.esporx.repositories.RoleRepository;
import tv.esporx.services.EmailingService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/user")
public class EsporxerController {

    private static final Logger LOGGER = getLogger(EsporxerController.class);
    private static final String COMMAND = "userCommand";
    private final EsporxerRepository esporxerRepository;
    private final RoleRepository roleRepository;
    private final StandardPasswordEncoder passwordEncoder;
    private final EmailingService emailingService;
    private final DomainClassConverter<?> entityConverter;

    @Autowired
    public EsporxerController(EsporxerRepository esporxerRepository,
                              RoleRepository roleRepository,
                              StandardPasswordEncoder passwordEncoder,
                              EmailingService emailingService,
                              DomainClassConverter entityConverter) {
        this.esporxerRepository = esporxerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailingService = emailingService;
        this.entityConverter = entityConverter;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields("password*", "email");
    }

    @RequestMapping(value = "/register", method = GET)
    public ModelAndView displayRegister(@ModelAttribute("userCommand") Esporxer user) {
        return new ModelAndView("user/register");
    }

    @RequestMapping(value = "/register", method = POST)
    public ModelAndView register(@ModelAttribute(COMMAND) @Valid Esporxer user,
                                 final BindingResult result,
                                 final RedirectAttributes redirectionAttributes) {
        ModelAndView view = new ModelAndView("user/register");
        if (!result.hasErrors()) {
            esporxerRepository.save(prePersist(user));
            emailingService.sendMessage(user.getEmail(), user.getAccountConfirmationHash());
            redirectionAttributes.addFlashAttribute("flash", "Please first confirm your account: a confirmation email has been sent.");
            view = new ModelAndView("redirect:/admin/login");
        }
        return view;
    }

    @RequestMapping(value = "/confirm", method = GET)
    public ModelAndView confirm(@RequestParam("hash") String hash,
                                @RequestParam("email") String email,
                                RedirectAttributes redirectionAttributes) {
        Esporxer esporxer = esporxerRepository.findByEmailAndAccountConfirmationHashAndAccountCreationDateAfter(email, hash, new DateTime().minusWeeks(2).toDate());
        if (esporxer == null) {
            redirectionAttributes.addFlashAttribute("flash", "Confirmation mismatch: your account has NOT been confirmed.");
        } else {
            esporxerRepository.save(preEnable(esporxer));
            redirectionAttributes.addFlashAttribute("flash", "Congratz! Your account is confirmed, you can now log in" );
        }

        return new ModelAndView("redirect:/admin/login");
    }

    @RequestMapping(value = "/profile", method = GET)
    public ModelAndView profile() {
        ModelAndView view = new ModelAndView("user/profile");
        view.addObject(COMMAND, getContext().getAuthentication().getPrincipal());
        return view;
    }

    @RequestMapping(value = "/profile", method = POST)
    public ModelAndView profile(@ModelAttribute(COMMAND) @Valid Esporxer user,
                                BindingResult bindingResult,//this must follow the object under validation
                                @RequestParam("oldPassword") String oldPassword,
                                RedirectAttributes redirectAttributes) {
        ModelAndView view = new ModelAndView("user/profile");
        Map<String, String> errors = validateFurther(user, oldPassword);

        if (bindingResult.hasErrors() || !errors.isEmpty()) {
            for (Map.Entry<String, String> error : errors.entrySet()) {
                view.addObject("err_"+error.getKey(), error.getValue());
            }
        }
        else {
            redirectAttributes.addFlashAttribute("flash", "Update successful");
            esporxerRepository.save(preUpdate(user));
            view.setViewName("redirect:/user/profile");
        }
        return view;
    }

    private Map<String, String> validateFurther(Esporxer user, String oldPassword) {
        HashMap<String, String> errors = newHashMap();
        Esporxer loggedInUser = (Esporxer) getContext().getAuthentication().getPrincipal();
        if (!user.getEmail().equals(loggedInUser.getEmail())) {
            LOGGER.warn(loggedInUser.getEmail() + " has tried to modify " + user.getEmail() + "'s account");
            errors.put("email", "trying to modify another account?");
            //do not check for anything else in case of fraud attempt like this one
        }
        else {
            if (!passwordEncoder.matches(oldPassword, loggedInUser.getPassword())) {
                errors.put("oldPassword", "password mismatch");
            }
        }
        return errors;
    }

    private Esporxer prePersist(Esporxer user) {
        user = preUpdate(user);
        user.setAuthorities(newHashSet(roleRepository.findByAuthority("ROLE_USER")));
        user.setAccountConfirmationHash(new HashGenerator().generateHash());
        return user;
    }

    private Esporxer preUpdate(Esporxer user) {
        Esporxer loggedInUser = (Esporxer) getContext().getAuthentication().getPrincipal();

        Esporxer persistedUser = esporxerRepository.findByEmail(loggedInUser.getEmail());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        persistedUser.setPassword(encodedPassword);
        persistedUser.setPasswordConfirmation(encodedPassword);
        return persistedUser;
    }

    private Esporxer preEnable(Esporxer user) {
        user.setEnabled(true);
        user.setPasswordConfirmation(user.getPassword());
        return user;
    }
}
