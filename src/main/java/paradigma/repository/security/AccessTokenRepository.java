package paradigma.repository.security;

import org.springframework.data.repository.CrudRepository;
import paradigma.entity.security.AccessToken;

/**
 * Created on 5/22/16.
 */
public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {
}
