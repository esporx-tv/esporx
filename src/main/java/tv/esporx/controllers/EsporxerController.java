package tv.esporx.controllers;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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

import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/user")
public class EsporxerController {

    private final EsporxerRepository esporxerRepository;
    private final RoleRepository roleRepository;
    private final StandardPasswordEncoder passwordEncoder;
    private final EmailingService emailingService;

    @Autowired
    public EsporxerController(EsporxerRepository esporxerRepository, RoleRepository roleRepository, StandardPasswordEncoder passwordEncoder, EmailingService emailingService) {
        this.esporxerRepository = esporxerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailingService = emailingService;
    }

    @RequestMapping(value = "/register", method = GET)
    public ModelAndView displayRegister(@ModelAttribute("userCommand") Esporxer user) {
        return new ModelAndView("user/register");
    }

    @RequestMapping(value = "/register", method = POST)
    public ModelAndView register(@ModelAttribute("userCommand") @Valid Esporxer user,
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
            esporxerRepository.save(preUpdate(esporxer));
            redirectionAttributes.addFlashAttribute("flash", "Congratz! Your account is confirmed, you can now log in" );
        }

        return new ModelAndView("redirect:/admin/login");
    }

    private Esporxer prePersist(Esporxer user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setPasswordConfirmation(encodedPassword);
        user.setAuthorities(newHashSet(roleRepository.findByAuthority("ROLE_USER")));
        user.setAccountConfirmationHash(new HashGenerator().generateHash());
        return user;
    }

    private Esporxer preUpdate(Esporxer user) {
        user.setEnabled(true);
        user.setPasswordConfirmation(user.getPassword());
        return user;
    }
}
