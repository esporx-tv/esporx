package tv.esporx.collections.functions;

import com.google.common.base.Function;
import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityAsRole implements Function<GrantedAuthority, String> {

    @Override
    public String apply(GrantedAuthority authority) {
        return authority.getAuthority();
    }
}
