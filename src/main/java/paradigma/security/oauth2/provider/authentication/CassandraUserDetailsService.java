package paradigma.security.oauth2.provider.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import paradigma.entity.security.User;
import paradigma.repository.security.UserRepository;
import paradigma.security.GrantedAuthorityCreater;

/**
 * Created on 5/22/16.
 */
@Service
public class CassandraUserDetailsService implements UserDetailsService {

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findOne(username);
        return user != null ? new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), user.isActive(),
                true, true, true, grantedAuthorityCreater.fromCollection(user.getRoles())) : null;
    }

}
