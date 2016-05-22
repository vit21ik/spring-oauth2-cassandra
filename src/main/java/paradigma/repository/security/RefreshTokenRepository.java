package paradigma.repository.security;

import org.springframework.data.repository.CrudRepository;
import paradigma.entity.security.RefreshToken;

/**
 * Created on 5/22/16.
 */
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
