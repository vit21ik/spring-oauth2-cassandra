package paradigma.repository.security;

import org.springframework.data.repository.CrudRepository;
import paradigma.entity.security.Client;

/**
 * Created on 5/22/16.
 */
public interface ClientRepository extends CrudRepository<Client, String> {
}
