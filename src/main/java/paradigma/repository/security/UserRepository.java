package paradigma.repository.security;

import org.springframework.data.repository.CrudRepository;
import paradigma.entity.security.User;

public interface UserRepository extends CrudRepository<User, String> {
}
