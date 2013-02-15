package tv.esporx.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tv.esporx.repositories.EsporxerRepository;


@Service
@Transactional
public class EsporxerService implements UserDetailsService {

    @Autowired
    private EsporxerRepository esporxerRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return esporxerRepository.findByEmail(email);
    }

}
