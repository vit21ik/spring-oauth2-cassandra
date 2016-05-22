package paradigma.security.oauth2.provider.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import paradigma.entity.security.User;
import paradigma.repository.security.UserRepository;
import paradigma.security.GrantedAuthorityCreater;

@Primary
@Component
public class CassandraAuthenticationManager implements AuthenticationManager {

    private UserRepository userRepository;

    private GrantedAuthorityCreater grantedAuthorityCreater = new GrantedAuthorityCreater();

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setGrantedAuthorityCreater(GrantedAuthorityCreater grantedAuthorityCreater) {
        this.grantedAuthorityCreater = grantedAuthorityCreater;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final User user = userRepository.findOne(username);
        if (user != null) {
            return new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword(), grantedAuthorityCreater.fromCollection(user.getRoles()));
        }
        return null;
    }

}
