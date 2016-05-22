package paradigma.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created on 5/22/16.
 */
@Component
public class GrantedAuthorityCreater {

    @Bean
    public GrantedAuthorityCreater getGrantedAuthorityCreater() {
        return new GrantedAuthorityCreater();
    }

    public List<SimpleGrantedAuthority> fromCollection(Set<String> roles) {
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (roles.contains("admin")) {
            authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return Collections.unmodifiableList(authList);

    }


}
