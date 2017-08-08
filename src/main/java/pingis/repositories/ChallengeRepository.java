package pingis.repositories;

import pingis.entities.Challenge;
import org.springframework.data.repository.CrudRepository;

public interface ChallengeRepository extends CrudRepository<Challenge, Long> {

    Challenge findByName(String name);

}
