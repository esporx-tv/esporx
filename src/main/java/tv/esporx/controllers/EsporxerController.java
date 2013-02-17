package tv.esporx.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import tv.esporx.domain.Esporxer;
import tv.esporx.repositories.EsporxerRepository;
import tv.esporx.repositories.RoleRepository;

import javax.validation.Valid;

import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class EsporxerController {

    private final EsporxerRepository esporxerRepository;
    private final RoleRepository roleRepository;
    private final StandardPasswordEncoder passwordEncoder;

    @Autowired
    public EsporxerController(EsporxerRepository esporxerRepository, RoleRepository roleRepository, StandardPasswordEncoder passwordEncoder) {
        this.esporxerRepository = esporxerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/user/register", method = GET)
    public ModelAndView displayRegister(@ModelAttribute("userCommand") Esporxer user) {
        return new ModelAndView("user/register");
    }

    @RequestMapping(value = "/user/register", method = POST)
    public ModelAndView register(@ModelAttribute("userCommand") @Valid Esporxer user, final BindingResult result) {
        ModelAndView view = new ModelAndView("user/register");
        if (!result.hasErrors()) {
            prePersist(user);
            esporxerRepository.save(user);
            view = new ModelAndView("redirect:/home");
        }
        return view;
    }

    private void prePersist(Esporxer user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setPasswordConfirmation(encodedPassword);
        user.setAuthorities(newHashSet(roleRepository.findByAuthority("ROLE_USER")));
    }
}
