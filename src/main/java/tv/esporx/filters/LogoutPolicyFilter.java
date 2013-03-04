package tv.esporx.filters;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import tv.esporx.collections.functions.GrantedAuthorityAsRole;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

import static com.google.common.collect.Collections2.transform;

public class LogoutPolicyFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Collection<String> roles = transform(authentication.getAuthorities(), new GrantedAuthorityAsRole());
            if (!roles.contains("ROLE_ADMIN")) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                httpRequest.getSession().setMaxInactiveInterval(3 * 60 * 60);
            }
        }


        chain.doFilter(request, response);
    }
}
