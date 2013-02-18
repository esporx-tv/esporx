package tv.esporx.services;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tv.esporx.domain.Esporxer;
import tv.esporx.repositories.EsporxerRepository;

import static org.slf4j.LoggerFactory.getLogger;


@Service
@Transactional
public class EsporxerService implements UserDetailsService {

    private static final Logger LOGGER = getLogger(EsporxerService.class);

    @Autowired
    private EsporxerRepository esporxerRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Esporxer esporxer = esporxerRepository.findByEmail(withDefaultDomain(email));
        LOGGER.info("Trying to log in: " + obfuscate(esporxer.getUsername()));
        return esporxer;
    }

    private String withDefaultDomain(String email) {
        email += !email.contains("@") ? "@esporx.com" : "";
        return email;
    }

    private String obfuscate(String username) {
        String atom = Splitter.on('@').split(username).iterator().next();
        return atom + "@__HIDDEN__";
    }

}
