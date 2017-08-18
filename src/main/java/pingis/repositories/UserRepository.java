package pingis.repositories;

import org.springframework.data.repository.CrudRepository;
import pingis.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

  User findByName(String name);
}