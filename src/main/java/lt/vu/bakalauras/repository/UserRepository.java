package lt.vu.bakalauras.repository;

import lt.vu.bakalauras.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
}
