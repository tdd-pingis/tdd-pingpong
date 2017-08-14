package pingis.repositories;

import org.springframework.data.repository.CrudRepository;
import pingis.entities.Challenge;

public interface ChallengeRepository extends CrudRepository<Challenge, Long> {

  Challenge findByName(String name);
}
